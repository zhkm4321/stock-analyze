package com.sword.springboot.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;

public abstract class ReflectionUtil {

  private static LRUCache<String, Map<String, Field>> classCache = new LRUCache<>(50);

  /**
   * 通过类对象，运行指定方法
   * 
   * @param obj
   *          类对象
   * @param methodName
   *          方法名
   * @param params
   *          参数值
   * @return 失败返回null
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static Object invokeMethod(Object obj, String methodName, Object[] params) throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    if (null == obj || StringUtils.isNullOrEmpty(methodName)) {
      return null;
    }
    Class<?> claszz = obj.getClass();
    Object resultObj = null;
    Class<?>[] paramTypes = null;

    if (params != null) {
      paramTypes = new Class[params.length];
      for (int i = 0; i < params.length; i++) {
        paramTypes[i] = params[i].getClass();
      }
    }
    Method method = claszz.getMethod(methodName, paramTypes);
    method.setAccessible(true);
    resultObj = method.invoke(obj, params);

    return resultObj;
  }

  /**
   * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
   * 
   * @param object
   *          : 子类对象
   * @param fieldName
   *          : 父类中的属性名
   * @param value
   *          : 将要设置的值
   */
  public static void setFieldValue(Class clazz, Object object, String fieldName, Object value) {
    // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
    Field field = getDeclaredField(clazz, object, fieldName);
    // 抑制Java对其的检查
    field.setAccessible(true);
    try {
      // 将 object 中 field 所代表的值 设置为 value
      field.set(object, value);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
   * 
   * @param object
   *          : 子类对象
   * @param fieldName
   *          : 父类中的属性名
   * @return : 父类中的属性值
   */
  public static Object getFieldValue(Class clazz, Object object, String fieldName) {
    // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
    Field field = getDeclaredField(clazz, object, fieldName);
    // 抑制Java对其的检查
    field.setAccessible(true);
    try {
      // 获取 object 中 field 所代表的属性值
      return field.get(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 循环向上转型, 获取对象的 DeclaredField
   * 
   * @param object
   *          : 子类对象
   * @param fieldName
   *          : 父类中的属性名
   * @return 父类中的属性对象
   */
  public static Field getDeclaredField(Class<?> clazz, Object object, String fieldName) {
    Map<String, Field> fieldDeclaredCache = classCache.get(clazz.getName());
    if (null == fieldDeclaredCache) {
      fieldDeclaredCache = new HashMap<String, Field>();
      classCache.put(clazz.getName(), fieldDeclaredCache);
    }
    Field field = fieldDeclaredCache.get(fieldName);
    if (null == field) {
      for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
        try {
          field = clazz.getDeclaredField(fieldName);
          fieldDeclaredCache.put(fieldName, field);
          // classCache.put(clazz.getName(), fieldDeclaredCache);
          return field;
        } catch (Exception e) {
          // 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
          // 如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
        }
      }
    }
    return field;
  }

  /**
   * 反射获取对象所有属性列表
   * 
   * @param model
   * @return
   */
  public static List<String> getFieldNameList(Object model) {
    List<String> propList = new ArrayList<String>();
    Field[] fields = model.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (!field.getName().equals("serialVersionUID")) {
        propList.add(field.getName());
      }
    }
    return propList;
  }

  /*
   * 将父类所有的属性COPY到子类中。 类定义中child一定要extends father；
   * 而且child和father一定为严格javabean写法，属性为deleteDate，方法为getDeleteDate
   */
  public static void fatherToChild(Object father, Object child) {
    if (!(child.getClass().getSuperclass() == father.getClass())) {
      System.err.println("child不是father的子类");
    }
    Class<?> fatherClass = father.getClass();
    Field ff[] = fatherClass.getDeclaredFields();
    for (int i = 0; i < ff.length; i++) {
      Field f = ff[i];// 取出每一个属性，如deleteDate
      Class<?> type = f.getType();
      try {
        if (f.getName().equals("serialVersionUID"))
          continue;
        Method m = fatherClass.getMethod("get" + upperHeadChar(f.getName()));// 方法getDeleteDate
        Object obj = m.invoke(father);// 取出属性值
        // 抑制Java对其的检查
        f.setAccessible(true);
        f.set(child, obj);
      } catch (SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  /**
   * 深度对象克隆方法
   * 
   * @param dest
   * @param orig
   * @param addition
   *          是否增量克隆，如果该是增量克隆将不会克隆空值属性
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static void copyProperties(Object dest, Object orig, Boolean addition)
      throws IllegalAccessException, InvocationTargetException {
    // Validate existence of the specified beans
    if (dest == null) {
      throw new IllegalArgumentException("No destination bean specified");
    }
    if (orig == null) {
      throw new IllegalArgumentException("No origin bean specified");
    }
    // Copy the properties, converting as necessary
    if (orig instanceof Map) {
      Iterator entries = ((Map) orig).entrySet().iterator();
      while (entries.hasNext()) {
        Map.Entry entry = (Map.Entry) entries.next();
        String name = (String) entry.getKey();
        if (BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(dest, name)) {
          if (null != addition && addition) {
            if (null != entry.getValue()) {
              BeanUtilsBean.getInstance().copyProperty(dest, name, entry.getValue());
            }
          } else {
            BeanUtilsBean.getInstance().copyProperty(dest, name, entry.getValue());
          }
        }
      }
    } else /* if (orig is a standard JavaBean) */ {
      PropertyDescriptor[] origDescriptors = BeanUtilsBean.getInstance().getPropertyUtils()
          .getPropertyDescriptors(orig);
      for (int i = 0; i < origDescriptors.length; i++) {
        String name = origDescriptors[i].getName();
        if ("class".equals(name)) {
          continue; // No point in trying to set an object's class
        }
        if (BeanUtilsBean.getInstance().getPropertyUtils().isReadable(orig, name)
            && BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(dest, name)) {
          try {
            Object value = BeanUtilsBean.getInstance().getPropertyUtils().getSimpleProperty(orig, name);
            if (null != addition && addition) {
              if (null != value) {
                BeanUtilsBean.getInstance().copyProperty(dest, name, value);
              }
            } else {
              BeanUtilsBean.getInstance().copyProperty(dest, name, value);
            }
          } catch (NoSuchMethodException e) {
            // Should not happen
          }
        }
      }
    }
  }

  public static boolean compare(Object source, Object target) throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    Class sourceClass = source.getClass();// 得到对象的Class
    Class targetClass = target.getClass();// 得到对象的Class

    Field[] sourceFields = sourceClass.getDeclaredFields();// 得到Class对象的所有属性
    Field[] targetFields = targetClass.getDeclaredFields();// 得到Class对象的所有属性
    boolean isSame = true;
    for (Field sourceField : sourceFields) {
      String name = sourceField.getName();// 属性名
      if (name.equalsIgnoreCase("serialVersionUID")) {
        continue;
      }
      Class type = sourceField.getType();// 属性类型
      String methodName = name.substring(0, 1).toUpperCase() + name.substring(1);
      Method sourceMethod = sourceClass.getMethod("get" + methodName);// 得到source属性对应get方法
      Method targetMethod = targetClass.getMethod("get" + methodName);// 得到target属性对应get方法
      Object sValue = sourceMethod.invoke(source);// 执行source对象的get方法得到属性值
      Object tValue = targetMethod.invoke(target);// 执行target对象的get方法得到属性值
      if (sValue != null && tValue != null) {
        if (!sValue.equals(tValue)) {
          isSame = false;
          break;
        }
      }
    }
    return isSame;
  }

  /**
   * 首字母大写，in:deleteDate，out:DeleteDate
   */
  private static String upperHeadChar(String in) {
    String head = in.substring(0, 1);
    String out = head.toUpperCase() + in.substring(1, in.length());
    return out;
  }

  /**
   * 获取一个类的class文件所在的绝对路径。 这个类可以是JDK自身的类，也可以是用户自定义的类，或者是第三方开发包里的类。
   * 只要是在本程序中可以被加载的类，都可以定位到它的class文件的绝对路径。
   * 
   * @param cls
   *          一个对象的Class属性
   * @return 这个类的class文件位置的绝对路径。 如果没有这个类的定义，则返回null。
   */
  public static String getPathFromClass(Class cls) throws IOException {
    String path = null;
    if (cls == null) {
      throw new NullPointerException();
    }
    URL url = getClassLocationURL(cls);
    if (url != null) {
      path = url.getPath();
      if ("jar".equalsIgnoreCase(url.getProtocol())) {
        try {
          path = new URL(path).getPath();
        } catch (MalformedURLException e) {
        }
        int location = path.indexOf("!/");
        if (location != -1) {
          path = path.substring(0, location);
        }
      }
      File file = new File(path);
      path = file.getCanonicalPath();
    }
    return path;
  }

  /**
   * 获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。
   */
  private static URL getClassLocationURL(final Class<?> cls) {
    if (cls == null)
      throw new IllegalArgumentException("null input: cls");
    URL result = null;
    final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
    final ProtectionDomain pd = cls.getProtectionDomain();
    // java.lang.Class contract does not specify
    // if 'pd' can ever be null;
    // it is not the case for Sun's implementations,
    // but guard against null
    // just in case:
    if (pd != null) {
      final CodeSource cs = pd.getCodeSource();
      // 'cs' can be null depending on
      // the classloader behavior:
      if (cs != null)
        result = cs.getLocation();

      if (result != null) {
        // Convert a code source location into
        // a full class file location
        // for some common cases:
        if ("file".equals(result.getProtocol())) {
          try {
            if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
              result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
            else if (new File(result.getFile()).isDirectory())
              result = new URL(result, clsAsResource);
          } catch (MalformedURLException ignore) {
          }
        }
      }
    }

    if (result == null) {
      // Try to find 'cls' definition as a resource;
      // this is not
      // document．d to be legal, but Sun's
      // implementations seem to //allow this:
      final ClassLoader clsLoader = cls.getClassLoader();
      result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader.getSystemResource(clsAsResource);
    }
    return result;
  }

}

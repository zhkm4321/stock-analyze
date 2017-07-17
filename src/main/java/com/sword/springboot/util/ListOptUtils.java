package com.sword.springboot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
public class ListOptUtils {
  /**
   * 交集
   * 
   * @param l1
   * @param l2
   * @return
   */
  public static List intersect(List l1, List l2) {
    List list = new ArrayList(Arrays.asList(new Object[l1.size()]));
    Collections.copy(list, l1);
    list.retainAll(l2);
    return list;
  }

  /**
   * 并集
   * 
   * @param l1
   * @param l2
   * @return
   */
  public static List union(List l1, List l2) {
    List list = new ArrayList(Arrays.asList(new Object[l1.size()]));
    Collections.copy(list, l1);
    list.addAll(l2);
    return list;
  }

  /**
   * 差集
   * 
   * @param l1
   * @param l2
   * @return
   */
  public static List diff(List l1, List l2) {
    List list = new ArrayList(Arrays.asList(new Object[l1.size()]));
    Collections.copy(list, l1);
    list.removeAll(l2);
    return list;
  }
  
  public static <T> T getUnique(List<T> list){
    T element = null;
    if(CollectionUtils.isEmpty(list)){
      
    }else if(list.size()==1){
      for (Iterator<T> it = list.iterator(); it.hasNext();) {
        element = (T) it.next();
      }
    }else{
      throw new RuntimeException("集合不唯一");
    }
    return element;
  }
}

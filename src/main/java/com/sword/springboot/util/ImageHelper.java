package com.sword.springboot.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageHelper {
  /*
   * 根据尺寸图片居中裁剪
   */
  public static void cutCenterImage(String src, String dest, int w, int h) throws IOException {
    // 缩放图片
    File srcFile = new File(src);
    BufferedImage bufImg = ImageIO.read(srcFile);
    BufferedImage itemp = resize(bufImg, w, h);
    int resizeW = itemp.getWidth();
    int resizeH = itemp.getHeight();
    int x = 0, y = 0;
    if (resizeW >= resizeH) {
      x = resizeW / 2 - w / 2;
    }
    else {
      y = resizeH / 2 - h / 2;
    }
    // 裁剪图片
    BufferedImage cutedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Graphics g = cutedImage.getGraphics();
    g.drawImage(itemp, 0, 0, w, h, x, y, w + x, h + y, null);
    g.dispose();
    ImageIO.write(cutedImage, "jpg", new File(dest));
  }

  /*
   * 图片裁剪二分之一
   */
  public static void cutHalfImage(String src, String dest) throws IOException {
    Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
    ImageReader reader = (ImageReader) iterator.next();
    InputStream in = new FileInputStream(src);
    ImageInputStream iis = ImageIO.createImageInputStream(in);
    reader.setInput(iis, true);
    ImageReadParam param = reader.getDefaultReadParam();
    int imageIndex = 0;
    int width = reader.getWidth(imageIndex) / 2;
    int height = reader.getHeight(imageIndex) / 2;
    Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
    param.setSourceRegion(rect);
    BufferedImage bi = reader.read(0, param);
    ImageIO.write(bi, "jpg", new File(dest));
  }
  /*
   * 图片裁剪通用接口
   */

  public static void cutImage(String src, String dest, int x, int y, int w, int h) throws IOException {
    Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
    ImageReader reader = (ImageReader) iterator.next();
    InputStream in = new FileInputStream(src);
    ImageInputStream iis = ImageIO.createImageInputStream(in);
    reader.setInput(iis, true);
    ImageReadParam param = reader.getDefaultReadParam();
    Rectangle rect = new Rectangle(x, y, w, h);
    param.setSourceRegion(rect);
    BufferedImage bi = reader.read(0, param);
    ImageIO.write(bi, "jpg", new File(dest));

  }

  /*
   * 图片缩放
   */
  public static void zoomImage(String src, String dest, int w, int h) throws Exception {
    double wr = 0, hr = 0;
    File srcFile = new File(src);
    File destFile = new File(dest);
    BufferedImage bufImg = ImageIO.read(srcFile);
    Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
    wr = w * 1.0 / bufImg.getWidth();
    hr = h * 1.0 / bufImg.getHeight();
    AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
    Itemp = ato.filter(bufImg, null);
    try {
      ImageIO.write((BufferedImage) Itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public static void convertPNG2JPG(String srcPath, String destPath) {
    BufferedImage bufferedImage;
    try {
      // read image file
      bufferedImage = ImageIO.read(new File(srcPath));
      // create a blank, RGB, same width and height, and a white background
      BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
          BufferedImage.TYPE_INT_RGB);
      // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
      newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
      // write to jpeg file
      ImageIO.write(newBufferedImage, "jpg", new File(destPath));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 实现图像的等比缩放
   * 
   * @param source
   * @param targetW
   * @param targetH
   * @return
   */
  private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
    // targetW，targetH分别表示目标长和宽
    int type = source.getType();
    BufferedImage target = null;
    double sx = (double) targetW / source.getWidth();
    double sy = (double) targetH / source.getHeight();
    // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
    // 则将下面的if else语句注释即可
    if (sx < sy) {
      sx = sy;
      targetW = (int) (sx * source.getWidth());
    }
    else {
      sy = sx;
      targetH = (int) (sy * source.getHeight());
    }
    if (type == BufferedImage.TYPE_CUSTOM) { // handmade
      ColorModel cm = source.getColorModel();
      WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
      boolean alphaPremultiplied = cm.isAlphaPremultiplied();
      target = new BufferedImage(cm, raster, alphaPremultiplied, null);
    }
    else
      target = new BufferedImage(targetW, targetH, type);
    Graphics2D g = target.createGraphics();
    // smoother than exlax:
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
    g.dispose();
    return target;
  }
}

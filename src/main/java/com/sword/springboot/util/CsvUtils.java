package com.sword.springboot.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvUtils {
  /**
   * 读取csv
   * @param httpUrl
   * @param maps
   * @param encoding
   * @return
   */
  public static List<String[]> readCsvFromUrl(String httpUrl,Map<String, String> maps, String encoding) {

    List<String[]> list = new ArrayList<String[]>();

    ByteArrayInputStream input = null;
    InputStreamReader reader = null;
    BufferedReader bReader = null;
    try {
      String csvContent = HttpClientUtils.sendHttpGet(httpUrl, maps, encoding);
      input = new ByteArrayInputStream(csvContent.getBytes(encoding));
      if (encoding == null) {
        reader = new InputStreamReader(input);
      } else {
        reader = new InputStreamReader(input, encoding);
      }
      bReader = new BufferedReader(reader);
      String str = bReader.readLine();
      String cellContent = "";
      Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
      while ((str = bReader.readLine()) != null) {
        if (!str.endsWith(",")) {
          str = str + ",";
        }
        Matcher mCells = pCells.matcher(str);
        List<String> cellList = new ArrayList<String>();
        // 读取每个单元格
        while (mCells.find()) {

          cellContent = mCells.group();
          cellContent = cellContent.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
          cellContent = cellContent.replaceAll("(?sm)(\"(\"))", "$2");
          cellList.add(cellContent);

        }
        list.add((String[]) cellList.toArray(new String[cellList.size()]));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != bReader) {
        try {
          bReader.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    return list;
  }
}

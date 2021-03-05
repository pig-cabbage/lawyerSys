package cabbage.project.lawyerSys.common.utils;

import java.util.Arrays;

public class StringUtils {

  public static Long stringToLong(String s) {
    StringBuilder temp = new StringBuilder();
    char[] chars = s.toCharArray();
    for (char c : chars) {
      int a = c - 1 + 49;
      if (a < 100) {
        temp.append(0);
        temp.append(a);
      } else {
        temp.append(a);
      }
    }
    return Long.valueOf(temp.toString());
  }
}

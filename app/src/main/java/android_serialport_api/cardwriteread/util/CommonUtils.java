package android_serialport_api.cardwriteread.util;

public class CommonUtils {

    /**
     * 姓名打*号
     * @param s
     * @return
     */
    public static String namePartReplaceStar(String s) {
        if (s.length() == 2) s = s.substring(0, 1) + "*";
        if (s.length() > 2) s = s.substring(0, 1) + "*" + s.substring(2, 3);
        return s;
    }


}

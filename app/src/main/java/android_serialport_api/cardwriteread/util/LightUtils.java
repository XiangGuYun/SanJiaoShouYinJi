package android_serialport_api.cardwriteread.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author 86139
 */
public class LightUtils {

    /**
     * WriteLightNode(1);//开补光灯
     * WriteLightNode(0);//关补光灯
     * @param value
     */
    public static void writeLightNode(int value) {
        String path1="/sys/devices/platform/body-sensor/light_ctl";
        File f1=new File(path1);
        String str1="";
        if(value == 1)
        {
            str1 = "1";
        }
        else
        {
            str1 = "0";
        }
        try {
            OutputStream output = null;
            OutputStreamWriter outputWrite = null;
            PrintWriter print = null;
            output = new FileOutputStream(f1);
            outputWrite = new OutputStreamWriter(output);
            print = new PrintWriter(outputWrite);
            print.print(str1);
            print.flush();
            print.close();
            outputWrite.close();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

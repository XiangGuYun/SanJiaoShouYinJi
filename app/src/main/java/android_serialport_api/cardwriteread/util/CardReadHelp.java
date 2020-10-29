package android_serialport_api.cardwriteread.util;

import java.io.IOException;
import java.io.OutputStream;

public class CardReadHelp {

    private static class SingleInstance{
        public static final CardReadHelp crh = new CardReadHelp();
    }

    public static CardReadHelp getInstance(){
        return SingleInstance.crh;
    }


    public String BytetoHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<b.length;i++) {
            sb.append(String.format("%02x ", b[i]));
        }
        return sb.toString();
    }

    public byte[] parase_text(String text_value)
    {
        int i=0;
        String str = "";
        for(i = 0;i < text_value.length();i++ )
        {
            if(text_value.charAt(i) != ' ' )
                str = str+text_value.charAt(i);
        }
        byte[] buf = new byte[str.length()/2];
        for(i = 0;i < str.length()/2;i++ )
        {
            buf[i] = (byte)(((charToByte(str.charAt(i*2)) << 4 )|(charToByte(str.charAt(i*2+1))))& 0xff);
        }
        return buf;
    }


    public byte charToByte(char val)
    {
        byte ret=0x0;
        if((val >= 'a') && (val<= 'z'))
        {
            ret = (byte)(val- 0x57);
        }
        else if((val >= 'A') && (val <= 'Z'))
        {
            ret = (byte)(val- 0x37);
        }
        else if((val >= '0') && (val<= '9'))
        {
            ret = (byte)(val- 0x30);
        }

        return ret;
    }

    public byte check_sum(byte[]  buf, int len)
    {
        byte sum = 0;
        int i=0;
        for(i=0;i<len;i++)
            sum += buf[i];
        return sum;
    }


}

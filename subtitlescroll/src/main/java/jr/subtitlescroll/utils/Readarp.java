package jr.subtitlescroll.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Shinelon on 2017/9/28.
 */

public class Readarp {
    private static final String TAG = "Readarp";
    String MacAddr = null;
    BufferedReader reader = null;

    public void read() {
        try {
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            String line = reader.readLine();
            Log.e(TAG, "read: " + line);
            //读取第一行信息，就是IP address HW type Flags HW address Mask Device
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[ ]+");
                if (tokens.length < 6) {
                    continue;
                }
                String ip = tokens[0]; //ip
                String mac = tokens[3];  //mac 地址
                String flag = tokens[2];//表示连接状态
                Log.d(TAG, "正在扫描的IP地址为：" + ip + "mac地址为：" + mac);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
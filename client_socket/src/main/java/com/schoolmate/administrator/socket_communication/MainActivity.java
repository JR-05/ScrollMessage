package com.schoolmate.administrator.socket_communication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText sendData;
    public String s, ip;
    public Handler acHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    Toast.makeText(MainActivity.this, "找不到IP,请在同一个局域网内", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendData = (EditText) findViewById(R.id.tv_reciver);
//        WifiManager wifiManager = (WifiManager) getSystemService(MainActivity.this.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        ip = intToIp(ipAddress);
//        Log.i("MainActivity", "IP:" + ip);
    }

    public void send(View v) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    s = sendData.getText().toString();
                    Socket clientSocket = new Socket("192.168.43.1", 10086);
                    Log.i("Mainactivity", "链接服务器Success");
                    OutputStream output = clientSocket.getOutputStream();
                    output.write(s.getBytes());
                    output.flush();
                    output.close();
                    clientSocket.close();
                    Log.i("Mainactivity", "成功发送信息");

                } catch (IOException e) {
                    e.printStackTrace();
                    Message error = new Message();
                    error.what = 0;
                    acHandler.sendMessage(error);
                }
            }
        }.start();
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}

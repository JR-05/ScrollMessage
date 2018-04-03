package com.jr.administrator.phonemic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_reciver;
    private Button bt_send, bt_get;
    private ReciverService.MyBinder myBinder;
    private MyServiceConn myConn = new MyServiceConn();
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_reciver = (TextView) findViewById(R.id.tv_reciver);
        bt_send = (Button) findViewById(R.id.sendData);
        bt_get = (Button) findViewById(R.id.getData);
        bt_get.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        WifiManager wifiManager = (WifiManager) getSystemService(MainActivity.this.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
         ip = intToIp(ipAddress);
        Log.i("MainActivity", "IP:"+ip);
        Intent server = new Intent(MainActivity.this, ReciverService.class);
        bindService(server, myConn, BIND_AUTO_CREATE);
    }
    private String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myConn);
    }

    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (ReciverService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendData: {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        try {
                            Log.i("MainActivity", "onClick: " + InetAddress.getLocalHost().getAddress());
                            Socket clientSocket = new Socket("10.32.128.223", 9999);
                            OutputStream output = clientSocket.getOutputStream();
                            output.write(new String("我叫JR").getBytes());
                            output.flush();
                            output.close();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            }
            case R.id.getData: {
                tv_reciver.setText(myBinder.getData());
                break;
            }
        }

    }
}

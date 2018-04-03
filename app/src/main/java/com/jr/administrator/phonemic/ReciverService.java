package com.jr.administrator.phonemic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

/**
 * Created by Administrator on 2017/7/1.
 */

public class ReciverService extends Service {

    String data;
    public MyBinder myBinder = new MyBinder();

    class MyBinder extends Binder {
        public String getData() {
            return data;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ReciverService", "onBind: ");
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ReciverService", "onCreate: ");
        new Thread() {//由于主线程不能放一些阻碍线程的任务(网络访问)，只好放在子线程
            @Override
            public void run() {
                super.run();
                try {
                    ServerSocket serverSocket = new ServerSocket(9999);
                    Log.i("ReciverService", "开启了服务端");
                    while (true) {
                        final Socket clientSocket = serverSocket.accept();
                        Log.i("ReciverService", "来了一位客户端 ");
                        new Thread() {//创建一个子线程用来处理客户端的任务
                            @Override
                            public void run() {
                                super.run();
                                InputStream dataStream = null;
                                ByteArrayOutputStream bArrayOut = new ByteArrayOutputStream();
                                try {
                                    dataStream = clientSocket.getInputStream();
                                    int getByte;
                                    while ((getByte = dataStream.read()) != -1) {//当读到-1表示读到最后
                                        bArrayOut.write(getByte);
                                    }
                                    data = bArrayOut.toString();
                                    Log.i("ReciverService", "收到的信息:" + data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        dataStream.close();
                                        bArrayOut.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }
}

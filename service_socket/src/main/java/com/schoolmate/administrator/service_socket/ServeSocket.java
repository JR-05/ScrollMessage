package com.schoolmate.administrator.service_socket;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2017/7/6.
 */

public class ServeSocket extends android.app.Service {
    private ServerSocket service;
    private Messenger acHandler;
    Handler serviceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //当AC获取到Service的Messenger（信使）后，通过Service信使发送AC的的信使
            switch (msg.what) {
                case 0:
                    acHandler = msg.replyTo;
                    Log.i("Service", "handleMessage: 得到AC的Messenger（信使）");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service", "onCreate: ");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    service = new ServerSocket(10086);
                    while (true) {
                        //无限循环，接受客户端发过来的Socket，service.accept()是一个阻塞的方法，它会等待接受客户端来的Socket
                        final Socket client = service.accept();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    InputStream dataStream = null;
                                    dataStream = client.getInputStream();

                                    ByteArrayOutputStream bArrayOut = new ByteArrayOutputStream();
                                    int getByte;

                                    while ((getByte = dataStream.read()) != -1) {//当读到-1表示读到最后
                                        bArrayOut.write(getByte);
                                    }

                                    String data = bArrayOut.toString();
                                    Message send = new Message();
                                    send.what = 1;
                                    send.obj = data;
                                    //拿到AC的信使后，通过信息发送消息给AC
                                    acHandler.send(send);
                                    Log.i("ReciverService", "收到的信息:" + data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service", "onBind: ");
        //当Service与AC绑定事，会触发该方法，通过该方法传递该Service的Messenger（信使），给AC
        return new Messenger(serviceHandler).getBinder();
    }
}

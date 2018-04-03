package com.schoolmate.administrator.service_socket;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public Messenger sHandler;
    private TextView reciver;
    public Handler acHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    String getData = (String) msg.obj;
                    reciver.setText(getData);
                    Log.i("MainActivity", "handleMessage: " + getData);
                    break;

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Mainactivity", "onCreate: ");
        reciver = (TextView) findViewById(R.id.recive);
        Intent service = new Intent(MainActivity.this, ServeSocket.class);
        bindService(service, Myconn, BIND_AUTO_CREATE);
    }

    //绑定监听
    ServiceConnection Myconn = new ServiceConnection() {
        //当与Service绑定是就会触发该方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过该方法获取到Service的Messenger
            sHandler = new Messenger(service);
            //
            Messenger acMessenger = new Messenger(acHandler);
            Message send = new Message();
            send.what = 0;
            send.replyTo = acMessenger;
            try {
                //想Service发送AC的信使
                sHandler.send(send);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}

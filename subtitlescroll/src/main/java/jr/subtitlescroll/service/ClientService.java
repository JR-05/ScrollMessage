package jr.subtitlescroll.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import jr.subtitlescroll.MyAppliction;
import jr.subtitlescroll.utils.GetWifiInfo;

import static jr.subtitlescroll.TAG.*;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ClientService extends Service {


    private Socket client;
    private Messenger main_AC_Messenger;
    private Messenger sub_AC_Messenger;
    private String gateway;

    Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取Main_Activity的Messenger
                case BIND_MAIN_AC: {
                    Log.e("ClientService", "获取到AC_Messenger");
                    main_AC_Messenger = msg.replyTo;
                    break;
                }
                //获取Sub_Title_Activity的Messenger
                case BIND_SUB_AC: {
                    sub_AC_Messenger = msg.replyTo;
                    break;
                }
                //连接服务端
                case ONPEN_WIFI_CONNECTE_SERVICE: {
                    connectService();
                    break;
                }
                //连接服务端
                case CONNECT: {
                    gateway = (String) msg.obj;
                    connectService();
                    break;
                }
                //断开连接
                case DISCONNECT: {
                    if (client != null && client.isConnected()) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("key", KEY_DISCONNECT);
                                jsonObject.put("name", android.os.Build.BRAND);
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                                bw.write(jsonObject.toString() + "\n");
                                bw.newLine();
                                bw.flush();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case NEXT: {
                    nextScroll();
                    break;
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("ClientService", "onBind");
        gateway = intent.getStringExtra("gateway");
        return new Messenger(sHandler).getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("ClientService", "onCreate");
        //等待1秒，让Service的Messenger获取到AC的Messenger,否则会报空指针异常
        connectService();

    }

    //连接服务器
    private void connectService() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    final Socket socket = new Socket(gateway, 5555);
                    socket.setSoTimeout(0);
                    socket.setKeepAlive(true);
                    if (socket.isConnected()) {
                        //告诉MainActitivty，链接成功
                        connect_successfull();
                        client = socket;
                        //发送客户端手机信息给服务端
                        MyAppliction myAppliction = (MyAppliction) getApplication();
                        String phoneBrand = android.os.Build.BRAND;
                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("key", KEY_PHONE_MESSAGE);
                        jsonObject.put("brand", phoneBrand);
                        jsonObject.put("ip", GetWifiInfo.getInstance(getApplication()).getWifiInfo().get("ipAddress"));
                        jsonObject.put("offX", String.valueOf(myAppliction.width));
                        String msg = jsonObject.toString();
                        Log.e("ClientService", "客户端连接服务端第一次发送的手机信息:" + msg);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bw.write(msg + "\n");
                        bw.newLine();
                        bw.flush();
                        //创建新的线程接受来自服务端发送的信息
                        Client c = new Client(socket, new Client.RecivedMessage() {
                            @Override
                            public void recived(String data) {
                                Log.e("ClientService", data);
                                judgeMsg(data, socket);
                            }

                            @Override
                            public void disConnect() {
                                Message message = new Message();
                                message.what = DISCONNECT;
                                try {
                                    //发送信息到ClientService来断开服务器
                                    if (main_AC_Messenger != null) {
                                        Log.e("ClientService", "disConnect");
                                        main_AC_Messenger.send(message);
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        c.start();
                    } else {
                        connect_failed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ClientService", "IOException");
                    connect_failed();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ClientService", "JSONException");
                }
            }
        }, 1000);
    }

    //连接服务器成功，发送信号给AC修改UI
    public void connect_successfull() {
        Message message = new Message();
        message.what = CONNECT_SUCCESSFULL;
        try {
            main_AC_Messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //连接服务器失败，发送信号给AC修改UI
    public void connect_failed() {
        Message message = new Message();
        message.what = CONNECT_FAILED;
        try {
            main_AC_Messenger.send(message);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    //判断消息类型
    public void judgeMsg(String msg, Socket socket) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String key = jsonObject.getString("key");
            switch (key) {
                case KEY_POSITION: {
                    ((MyAppliction) getApplication()).position = jsonObject.getString("position");
                    break;
                }
                case KEY_SUBTITLE: {
                    Log.e("ClientService", "KEY_SUBTITLE");
                    String data = !jsonObject.isNull("subtitle") ? jsonObject.getString("subtitle") : "";
                    String offX = !jsonObject.isNull("offX") ? jsonObject.getString("offX") : "";
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("subtitle", data);
                    bundle.putString("offX", offX);
                    message.what = SET_TITLE;
                    message.setData(bundle);
                    sub_AC_Messenger.send(message);
                    break;
                }
                case KEY_START: {
                    Log.e("ClientService", "clientAccount:" + jsonObject.getString("clientAccount"));
                    ((MyAppliction) getApplication()).clientAccount = Integer.valueOf(jsonObject.getString("clientAccount"));
                    Message message = new Message();
                    message.what = SCOLLSUBTITLE;
                    sub_AC_Messenger.send(message);
                    break;
                }
                case KEY_NEXT_SCROLL: {
                    ((MyAppliction) getApplication()).clientAccount = Integer.valueOf(jsonObject.getString("clientAccount"));
                    Message message = new Message();
                    message.what = SCOLLSUBTITLE;
                    sub_AC_Messenger.send(message);
                    break;
                }
                case KEY_CHANG_TEXT_COLOR: {
                    Message message = new Message();
                    message.what = CHANGE_TEXT_COLOR;
                    message.obj = jsonObject.getString("colorcode");
                    sub_AC_Messenger.send(message);
                    break;
                }

                case KEY_DISCONNECT: {
                    socket.close();
                    String name = jsonObject.getString("name");
                    Log.e("ServerService", name + "与组织失去了联系");
                    Message message = new Message();
                    message.what = DISCONNECT;
                    message.obj = name;
                    try {
                        //发送信息到ClientService来断开服务器
                        if (main_AC_Messenger != null) {
                            Log.e("MainActivity", "disConnect");
                            main_AC_Messenger.send(message);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void nextScroll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject jsonObject = new JSONObject();
                try {
                    MyAppliction appliction = (MyAppliction) getApplication();
                    jsonObject.put("key", KEY_NEXT_Client_SCROLL);
                    jsonObject.put("position", appliction.position);
                    String msg = jsonObject.toString();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    bw.write(msg + "\n");
                    bw.newLine();
                    bw.flush();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

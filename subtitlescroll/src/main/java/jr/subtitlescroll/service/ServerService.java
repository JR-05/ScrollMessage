package jr.subtitlescroll.service;

import android.app.Service;
import android.content.Intent;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import jr.subtitlescroll.MyAppliction;

import static jr.subtitlescroll.TAG.*;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ServerService extends Service {
    private Messenger main_AC_Messenger;
    private Messenger refresh_AC_MESSENGER;
    private Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BIND_MAIN_AC: {
                    main_AC_Messenger = msg.replyTo;
                    break;
                }
                case BIND_REFRESHIP_AC: {
                    refresh_AC_MESSENGER = msg.replyTo;
                    break;
                }
                case DISCONNECT: {
                    for (HashMap<String, Object> object : ((MyAppliction) getApplication()).clientList) {
                        Socket socket = (Socket) object.get("socket");
                        try {
                            JSONObject json = new JSONObject();
                            json.put("key", KEY_DISCONNECT);
                            BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bWriter.write(json.toString() + "\n");
                            bWriter.newLine();
                            bWriter.flush();
                            bWriter.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        //等待1秒，让Service的Messenger获取到AC的Messenger,否则会报空指针异常
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(5555);
                    while (true) {
                        final Socket socket = serverSocket.accept();
                        //创建新的线程接受来自客户端发送的信息
                        Client client = new Client(socket, new Client.RecivedMessage() {
                            @Override
                            public void recived(String msg) {
                                Log.e("ServerService", "recived:" + msg);
                                judgeMsg(msg, socket);
                            }

                            @Override
                            public void disConnect() {
                            }
                        });
                        client.start();
                        connect_successfull();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(sHandler).getBinder();
    }

    //连接服务器成功，发送信号给AC修改UI
    public void connect_successfull() {
        Message message = new Message();
        message.what = CLIENT_CONNECTION;
        try {
            main_AC_Messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //判断消息类型
    public void judgeMsg(String msg, Socket socket) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String key = jsonObject.getString("key");
            switch (key) {
                case KEY_PHONE_MESSAGE: {
                    //获取来自客户端接受到的手机信息并保存
                    String phoneBrand = jsonObject.getString("brand");
                    String phoneIP = jsonObject.getString("ip");
                    String witdh = jsonObject.getString("offX");
                    MyAppliction appliction = (MyAppliction) getApplication();
                    HashMap<String, Object> client = new HashMap<String, Object>();
                    //更新Refresh_IP_Activity中的的ListView
                    Message message = new Message();
                    message.what = CLIENT_CONNECTION;
                    if (refresh_AC_MESSENGER != null) {
                        refresh_AC_MESSENGER.send(message);
                    }
                    //手机信息
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("bind", phoneBrand);
                    data.put("ip", phoneIP);
                    data.put("width", witdh);
                    //存放到Application中
                    client.put("phoneMsg", data);
                    client.put("socket", socket);
                    appliction.clientList.add(client);
                    //发送手机位置,并标记添加了一位客户端
                    BufferedWriter bw = null;
                    try {
                        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        JSONObject json = new JSONObject();
                        json.put("key", KEY_POSITION);
                        json.put("position", ++appliction.clientAccount);
                        bw.write(json.toString() + "\n");
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case KEY_DISCONNECT: {
                    String name = jsonObject.getString("name");
                    Log.e("ServerService", name + "与组织失去了联系");
                    Message message = new Message();
                    message.what = DISCONNECT;
                    message.obj = name;
                    try {
                        //发送信息到ClientService来断开服务器
                        if (main_AC_Messenger != null) {
                            Log.e("ServerService", "disConnect");
                            main_AC_Messenger.send(message);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case KEY_NEXT_Client_SCROLL: {
                    Log.e("ServerService", "KEY_NEXT_Client_SCROLL");
                    String currenPosition = jsonObject.getString("position");
                    nextScroll(currenPosition);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void nextScroll(String position) {
        MyAppliction appliction = (MyAppliction) getApplication();
        int nextPosition = Integer.valueOf(position);
        if (appliction.clientList.size() == 1) {
            return;
        } else if (nextPosition == (appliction.clientList.size())) {
            nextPosition = 0;
        }
        Log.e("ServerService", "nextPosition:" + nextPosition);
        HashMap<String, Object> clientList = appliction.clientList.get(nextPosition);
        Socket socket = (Socket) clientList.get("socket");
        //如果客户端与服务端保持连接就发送消息
        if (socket.isConnected()) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                JSONObject json = new JSONObject();
                json.put("key", KEY_NEXT_SCROLL);
                json.put("clientAccount", String.valueOf(clientList.size()) + 1);
                Log.e("ServerService", "clientList.size():" + clientList.size());
                bw.write(json.toString() + "\n");
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //否则移除客户端,并直通MainActivity刷新客户端信息
        else {
            appliction.clientList.remove(clientList);
            Message message = new Message();
            message.what = CLIENT_CONNECTION;
            try {
                refresh_AC_MESSENGER.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

}

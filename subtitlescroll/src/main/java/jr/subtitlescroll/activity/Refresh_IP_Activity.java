package jr.subtitlescroll.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jr.subtitlescroll.MyAppliction;
import jr.subtitlescroll.R;
import jr.subtitlescroll.adapter.IP_Adapter;
import jr.subtitlescroll.service.ServerService;
import jr.subtitlescroll.weight.AddPopupWindow;

import static jr.subtitlescroll.TAG.BIND_REFRESHIP_AC;
import static jr.subtitlescroll.TAG.CLIENT_CONNECTION;
import static jr.subtitlescroll.TAG.KEY_CHANG_TEXT_COLOR;
import static jr.subtitlescroll.TAG.KEY_START;
import static jr.subtitlescroll.TAG.KEY_SUBTITLE;

/**
 * Created by Administrator on 2017/9/28.
 */

public class Refresh_IP_Activity extends BaseActivity {


    @BindView(R.id.actionbar_back)
    ImageView actionbarBack;
    @BindView(R.id.actionbar_title)
    TextView actionbarTitle;
    @BindView(R.id.refresh_ip_ac_add_msg)
    Button refresh_ip_ac_add_msg;
    @BindView(R.id.actionbar_other)
    ImageView actionbarOther;
    @BindView(R.id.ip_list)
    ListView ipList;
    private AddPopupWindow addPopupWindow;
    private IP_Adapter ip_adapter;
    private ArrayList<HashMap<String, Object>> clientList;
    private Messenger sMessenger;
    private Timer change_TextColor_Timer;
    private int status = 0;
    Handler ac_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CLIENT_CONNECTION: {
                    ip_adapter.notifyDataSetChanged();
                    clientList = ((MyAppliction) getApplication()).clientList;
                    break;
                }
            }
        }
    };

    ServiceConnection myServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sMessenger = new Messenger(service);
            Messenger acMessenger = new Messenger(ac_Handler);
            Message message = new Message();
            message.what = BIND_REFRESHIP_AC;
            message.replyTo = acMessenger;
            try {
                sMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.refresh_activity);
        ButterKnife.bind(this);

    }

    @Override
    protected void initVariables() {
        clientList = ((MyAppliction) getApplication()).clientList;
        ip_adapter = new IP_Adapter(Refresh_IP_Activity.this, clientList);
        addPopupWindow = new AddPopupWindow(this);
//        addPopupWindow.showAtLocation(this.getCurrentFocus(), Gravity.NO_GRAVITY, 0, 0);
    }

    @Override
    protected void loadData() {
        ipList.setAdapter(ip_adapter);
        bindService(new Intent(Refresh_IP_Activity.this, ServerService.class), myServiceConn, Service.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myServiceConn);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.actionbar_back, R.id.actionbar_other, R.id.refresh_ip_ac_add_msg})
    public void onViewClicked(View view) {
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        int heightPixels = displayMetrics.heightPixels;
        ;
        switch (view.getId()) {
            case R.id.actionbar_back:
                Log.e("Refresh_IP_Activity", "actionbar_back: ");
                finish();
                break;
            case R.id.actionbar_other:
                Log.e("Refresh_IP_Activity", "actionbar_other: ");
                RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(1000);
                rotateAnimation.setRepeatCount(3);
                rotateAnimation.setRepeatMode(Animation.RESTART);
                view.startAnimation(rotateAnimation);
                ip_adapter.notifyDataSetChanged();
                break;
            case R.id.refresh_ip_ac_add_msg: {
                Log.e("Refresh_IP_Activity", "refresh_ip_ac_add_msg:" + status);
                switch (status) {
                    case 0: {
                        status++;
                        refresh_ip_ac_add_msg.setText("＋");
                        addPopupWindow.showPopupWindow(view);
                        backgroundAlpha((float) 0.85);
                        addPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                backgroundAlpha((float) 1.0);
                                refresh_ip_ac_add_msg.setText("√");
                            }
                        });
                        break;
                    }
                    case 1: {
                        status++;
                        refresh_ip_ac_add_msg.setText("↑");
                        String msg = addPopupWindow.editText.getText().toString();
                        //上传
                        EventBus.getDefault().post(msg);
                        //提示
                        Toast toast = Toast.makeText(this,
                                "已下达指令!!!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                    case 2: {
                        status = 0;
                        refresh_ip_ac_add_msg.setText("＋");
                        //开始轮播
                        start();
                        changTextColor();
                        //提示
                        Toast toast = Toast.makeText(this,
                                "开始行动！！！", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                }
                break;
            }
        }
    }


    /*
    *将文本数据上传到客户端
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void sendMsgToClient(String msg) {
        String width = "0";
        for (HashMap<String, Object> object : clientList) {
            HashMap<String, String> clientMsg = (HashMap<String, String>) object.get("phoneMsg");
            Socket socket = (Socket) object.get("socket");
            //如果客户端与服务端保持连接就发送消息
            if (socket.isConnected()) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("key", KEY_SUBTITLE);
                    json.put("subtitle", msg);
                    json.put("offX", width);
                    width = clientMsg.get("width");
                    Log.e("Refresh_IP_Activity", "width:" + width);
                    BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bWriter.write(json.toString() + "\n");
                    bWriter.newLine();
                    bWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }  //否则移除客户端
            else {
                ((MyAppliction) getApplication()).clientList.remove(clientMsg);
                Message message = new Message();
                message.what = CLIENT_CONNECTION;
                ac_Handler.sendMessage(message);
            }
        }

    }

    public void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                HashMap<String, Object> client = clientList.get(0);
                Socket socket = (Socket) client.get("socket");
                //如果客户端与服务端保持连接就发送消息
                if (socket.isConnected()) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("key", KEY_START);
                        json.put("clientAccount", clientList.size());
                        Log.e("ServerService", "clientList.size():" + clientList.size());
                        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bWriter.write(json.toString() + "\n");
                        bWriter.newLine();
                        bWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } //否则移除客户端
                else {
                    ((MyAppliction) getApplication()).clientList.remove(client);
                    Message message = new Message();
                    message.what = CLIENT_CONNECTION;
                    ac_Handler.sendMessage(message);
                }
            }
        }, 0);

    }

    //通知改变字体颜色

    public void changTextColor() {
        final ArrayList<Socket> Sockets = new ArrayList<Socket>();
        if (change_TextColor_Timer != null) {
            change_TextColor_Timer.cancel();
        }
        change_TextColor_Timer = new Timer();
        change_TextColor_Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (HashMap<String, Object> object : clientList) {
                    Socket socket = (Socket) object.get("socket");
                    //如果客户端与服务端保持连接就发送消息
                    if (socket.isConnected()) {
                        int i = 0;
                        try {
                            JSONObject json = new JSONObject();
                            json.put("key", KEY_CHANG_TEXT_COLOR);
                            json.put("colorcode", getRandColorCode());
                            BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bWriter.write(json.toString() + "\n");
                            bWriter.newLine();
                            bWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }  //否则移除客户端
                    else {
                        ((MyAppliction) getApplication()).clientList.remove(object);
                        Message message = new Message();
                        message.what = CLIENT_CONNECTION;
                        ac_Handler.sendMessage(message);
                    }
                }
            }
        }, 0, 200);

    }

    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     *
     * @return String
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        String rgb = "#" + r + g + b;
        return rgb;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(params);
    }
}

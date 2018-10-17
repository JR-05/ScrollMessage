package jr.subtitlescroll.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jr.subtitlescroll.MyAppliction;
import jr.subtitlescroll.R;
import jr.subtitlescroll.broadcast.MyWifiBroadcastReceiver;
import jr.subtitlescroll.service.ClientService;
import jr.subtitlescroll.service.ServerService;
import jr.subtitlescroll.utils.GetWifiInfo;

import static jr.subtitlescroll.TAG.BIND_MAIN_AC;
import static jr.subtitlescroll.TAG.CLIENT_CONNECTION;
import static jr.subtitlescroll.TAG.CONNECT;
import static jr.subtitlescroll.TAG.CONNECT_FAILED;
import static jr.subtitlescroll.TAG.CONNECT_SUCCESSFULL;
import static jr.subtitlescroll.TAG.DISCONNECT;

public class MainActivity extends BaseActivity {


    private boolean isWifiApEnabled, isWifiEnabled;
    private MyWifiBroadcastReceiver myWifiBroadcastReceiver;
    private ServiceConnection serviceConnection;
    private Messenger sMessenger;
    public Handler acHandler;

    @BindView(R.id.bt_control)
    Button btControl;
    @BindView(R.id.bt_subtitle)
    Button btSubtitle;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariables() {
        MyAppliction myAppliction = (MyAppliction) getApplication();
        Display display = getWindowManager().getDefaultDisplay();
        myAppliction.width = display.getWidth();
        isWifiApEnabled = GetWifiInfo.getInstance(mContext).isWifiApEnabled();
        acHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("MainActivity", "msg.what:" + msg.what);
                switch (msg.what) {
                    case CONNECT_SUCCESSFULL: {
                        Log.e("MainActivity", "CONNECT_SUCCESSFULL");
                        Toast.makeText(MainActivity.this, "跟指挥台取得联系", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, SubTitleActivity.class));
                        break;
                    }
                    case CONNECT_FAILED: {
                        Log.e("MainActivity", "CONNECT_FAILED");
                        Toast.makeText(MainActivity.this, "请打开WIFI连接指挥台", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case CLIENT_CONNECTION: {
                        Log.e("MainActivity", "CLIENT_CONNECTION");
                        Toast.makeText(MainActivity.this, "链接一位用户", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case DISCONNECT: {
                        Log.e("MainActivity", "DISCONNECT");
                        Toast.makeText(MainActivity.this, ((String) msg.obj) + "与组织失去了联系", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        };

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sMessenger = new Messenger(service);
                Messenger acMessenger = new Messenger(acHandler);
                Message message = new Message();
                message.what = BIND_MAIN_AC;
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
        //注册监听WIFI广播
        myWifiBroadcastReceiver = new MyWifiBroadcastReceiver(new MyWifiBroadcastReceiver.WifiStaus() {
            @Override
            public void connect() {
                isWifiEnabled = true;
            }

            @Override
            public void disConnect() {
                Message message = new Message();
                message.what = DISCONNECT;
                try {
                    //发送信息到ClientService来断开服务器
                    if (sMessenger != null) {
                        Log.d("MainActivity", "disConnect");
                        sMessenger.send(message);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        //判断是否开启wifi选择主客端
        if (isWifiApEnabled) {
            //服务端
            Intent intent = new Intent(MainActivity.this, ServerService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            btSubtitle.setEnabled(false);
        } else {
            //客户端
            Intent intent = new Intent(MainActivity.this, ClientService.class);
            //获取局域网网关
            String gateway = GetWifiInfo.getInstance(mContext).getWifiInfo().get("gateway");
            intent.putExtra("gateway", gateway);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
        //注册监听WIFI广播
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(myWifiBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开连接
        Message message = new Message();
        message.what = DISCONNECT;
        try {
            sMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(serviceConnection);
        unregisterReceiver(myWifiBroadcastReceiver);
    }

    @OnClick({R.id.bt_control, R.id.bt_subtitle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_control:
                //判断是开启服务端服务还是客户端服务
                isWifiApEnabled = GetWifiInfo.getInstance(mContext).isWifiApEnabled();
                if (isWifiApEnabled) {
                    //服务端
                    Intent intent = new Intent(MainActivity.this, ServerService.class);
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                    btSubtitle.setEnabled(false);
                    //进入设置界面
                    startActivity(new Intent(MainActivity.this, Refresh_IP_Activity.class));
                } else {
                    Toast.makeText(mContext, "请打开热点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_subtitle:
                String position = ((MyAppliction) getApplication()).position;
                if (position != null) {
                    startActivity(new Intent(MainActivity.this, SubTitleActivity.class));
                } else {
                    if (isWifiEnabled) {
                        String gateway = GetWifiInfo.getInstance(mContext).getWifiInfo().get("gateway");
                        Message message = new Message();
                        message.what = CONNECT;
                        message.obj = gateway;
                        try {
                            //发送信息到ClientService来链接服务器
                            if (sMessenger != null) {
                                Log.d("MainActivity", "connect");
                                sMessenger.send(message);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "请与指挥台取得联系", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

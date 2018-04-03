package jr.subtitlescroll.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import jr.subtitlescroll.MyAppliction;
import jr.subtitlescroll.R;
import jr.subtitlescroll.service.ClientService;

import static jr.subtitlescroll.TAG.BIND_SUB_AC;
import static jr.subtitlescroll.TAG.CHANGE_TEXT_COLOR;
import static jr.subtitlescroll.TAG.NEXT;
import static jr.subtitlescroll.TAG.SCOLLSUBTITLE;
import static jr.subtitlescroll.TAG.SET_TITLE;

/**
 * Created by Administrator on 2017/9/30.
 */

public class SubTitleActivity extends BaseActivity {

    private int position;
    private String subtitle, offX;
    public static int offx;
    private Messenger sMessenger;
    private ServiceConnection sConn;
    private Handler Sub_Handler;
    private Timer ready_Scoll_Timer, scoll_Subtitle_Timer;
    private MediaPlayer mediaPlayer;

    @BindView(R.id.subtitile_sub_ac)
    TextView subtitileSubAc;
    @BindView(R.id.horiSv)
    HorizontalScrollView horiSv;

    private boolean isReadToPlay;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Window window = getWindow();
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //设置屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.subtitle_activity);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariables() {
        String tPosition = ((MyAppliction) getApplication()).position;
        position = Integer.parseInt(tPosition);
        Sub_Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SET_TITLE: {
                        Bundle bundle = msg.getData();
                        subtitle = bundle.getString("subtitle");
                        offX = bundle.getString("offX");
                        Log.e("SubTitleActivity", "SET_TITLE：" + subtitle + "offX:" + offX);
                        setSubtitle();
                        break;
                    }
                    case SCOLLSUBTITLE: {
                        Log.e("SubTitleActivity", "SCOLLSUBTITLE");
                        scollSubtitle();
                        break;
                    }
                    case CHANGE_TEXT_COLOR: {
                        String colorCode = (String) msg.obj;
                        subtitileSubAc.setTextColor(Color.parseColor(colorCode));
                        break;
                    }
                }
            }
        };

        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sMessenger = new Messenger(service);
                Messenger Sub_Messenger = new Messenger(Sub_Handler);
                Message message = new Message();
                message.what = BIND_SUB_AC;
                message.replyTo = Sub_Messenger;
                try {
                    sMessenger.send(message);
                } catch (RemoteException e) {

                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        //不可滑动
        horiSv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //音频准备
        try {
            mediaPlayer = new MediaPlayer();
            AssetManager am = getAssets();//获得该应用的AssetManager
            AssetFileDescriptor afd = getAssets().openFd("music.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor());
            //设置循环播放
            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isReadToPlay = true;
                    Log.e("SubTitleActivity", "isReadToPlay");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void loadData() {
        Intent servic = new Intent(SubTitleActivity.this, ClientService.class);
        bindService(servic, sConn, Service.BIND_AUTO_CREATE);
        horiSv = (HorizontalScrollView) findViewById(R.id.horiSv);
        subtitileSubAc = (TextView) findViewById(R.id.subtitile_sub_ac);
        subtitileSubAc.setText(String.valueOf(position));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sConn);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //准备滚动
    private void setSubtitle() {
        Log.e("SubTitleActivity", "isReadToPlay:" + isReadToPlay);
        //播放音乐
        if (mediaPlayer.isPlaying()) {
            Log.e("SubTitleActivity", "isPlaying");
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else if (isReadToPlay) {
            Log.e("SubTitleActivity", "stat");
            mediaPlayer.start();
        }
        //取消之前的任务
        if (ready_Scoll_Timer != null) {
            ready_Scoll_Timer.cancel();
            ready_Scoll_Timer = null;
        }
        if (scoll_Subtitle_Timer != null) {
            scoll_Subtitle_Timer.cancel();
            scoll_Subtitle_Timer = null;
        }
        subtitileSubAc.setText("Ready.....");
        subtitileSubAc.post(new Runnable() {

            @Override
            public void run() {
                int width = horiSv.getWidth();
                offx = (subtitileSubAc.getWidth() / 2) + (width / 2);
                //放大动画
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
                scaleAnimation.setDuration(200);
                scaleAnimation.setRepeatCount(1);
                subtitileSubAc.startAnimation(scaleAnimation);
                ready_Scoll_Timer = new Timer();
                ready_Scoll_Timer.schedule(new TimerTask() {
                    int tOffX = offx;

                    @Override
                    public void run() {
                        subtitileSubAc.post(new Runnable() {
                            @Override
                            public void run() {
                                if (tOffX == -offx) {
                                    tOffX = offx;
                                }
                                subtitileSubAc.setTranslationX((tOffX--));
                            }
                        });
                    }
                }, 0, 5);
            }

        });

    }

    //开始滚动
    private void scollSubtitle() {
//        取消之前的任务
        if (ready_Scoll_Timer != null) {
            ready_Scoll_Timer.cancel();
        }
        if (scoll_Subtitle_Timer != null) {
            scoll_Subtitle_Timer.cancel();
        }
        Log.e("SubTitleActivity", "run:" + ((MyAppliction) getApplication()).position + "+++++++" + ((MyAppliction) getApplication()).clientAccount);
        subtitileSubAc.setVisibility(View.INVISIBLE);
        subtitileSubAc.setText(subtitle);
        subtitileSubAc.post(new Runnable() {
            @Override
            public void run() {
                final int width = horiSv.getWidth();
                offx = (int) ((subtitileSubAc.getWidth() / 2) + width / 2);
                scoll_Subtitle_Timer = new Timer();
                scoll_Subtitle_Timer.schedule(new TimerTask() {
                    int tOffX = offx;

                    @Override
                    public void run() {
                        subtitileSubAc.post(new Runnable() {
                            @Override
                            public void run() {
                                MyAppliction app = (MyAppliction) getApplication();
                                /*
                                如果是只有一台手机的话就无线循环
                                 */
                                if (app.position.equals("1") && app.clientAccount == 1) {
                                    Log.e("SubTitleActivity", "isFrist");
                                    if (tOffX == -offx) {
                                        tOffX = offx;
                                    }
                                }
                                /*
                                如果是最后一台手机，就当跑到截至在发送信号给下一台手机
                                 */
                                if (app.position.equals(String.valueOf(app.clientAccount))) {
                                    Log.e("SubTitleActivity", "isLast");
                                    if (tOffX == -offx) {
                                        Message message = new Message();
                                        message.what = NEXT;
                                        try {
                                            sMessenger.send(message);
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                /*
                                如果是中间的手机就当移动一个屏幕的尺寸就发送信号给下一部手机
                                 */
                                else if (tOffX == (offx - width)) {
                                    Log.e("SubTitleActivity", "NEXT");
                                    Message message = new Message();
                                    message.what = NEXT;
                                    try {
                                        sMessenger.send(message);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                                subtitileSubAc.setTranslationX((tOffX--));
                                subtitileSubAc.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                }, 0, 5);
            }
        });


    }

}

package jr.subtitlescroll.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import jr.subtitlescroll.R;

import static android.R.attr.width;

/**
 * Created by Administrator on 2017/10/10.
 */

public class Test extends BaseActivity {
    @BindView(R.id.subtitile_sub_ac)
    TextView subtitileSubAc;
    @BindView(R.id.horiSv)
    HorizontalScrollView horiSv;
    private TranslateAnimation mRigthToLeftAnim;
    private final static float SCOLL_V = 0.25f;
    Timer timer;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.test);
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void loadData() {
    }

    //开始滚动
    public void click(View vv) {
        Log.e("Test", "horiSv.getWidth():" + (subtitileSubAc.getWidth() / 2) + width / 2);
        int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int offX = (subtitileSubAc.getWidth() / 2) + width / 2-1000;
        subtitileSubAc.setTranslationX((offX));
        if (timer != null) {
            timer.cancel();
            timer=null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int X = offX;

            @Override
            public void run() {
                subtitileSubAc.post(new Runnable() {
                    @Override
                    public void run() {
//                mRigthToLeftAnim = new TranslateAnimation(0, -(subtitileSubAc.getWidth() + horiSv.getWidth() / 2), 0, 0);
//                mRigthToLeftAnim.setRepeatCount(1);
//                mRigthToLeftAnim.setInterpolator(new LinearInterpolator());
//                mRigthToLeftAnim.setDuration((long) ((horiSv.getWidth() + subtitileSubAc.getWidth()) / SCOLL_V));
//                subtitileSubAc.startAnimation(mRigthToLeftAnim);
//                Log.e("SubTitleActivity", "mNoticeScrollView.getWidth():" + (long) ((horiSv.getWidth() + subtitileSubAc.getWidth())));
                        Log.e("Test", "X:" + X);
                        if (X == -offX) {
                            X = offX;
                        }
//                        subtitileSubAc.setTranslationX((X--));
                    }
                });
            }
        }, 0, 5);

    }    //开始滚动

    private void scollSubtitle(float offX) {
    }
}

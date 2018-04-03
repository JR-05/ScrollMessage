package com.schoolmate.administrator.voicetranslate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Daudioclient extends Activity implements View.OnClickListener {
    private Button bt_start, bt_stop, bt_exit;
    public static final int MENU_START_ID = Menu.FIRST;
    public static final int MENU_STOP_ID = Menu.FIRST + 1;
    public static final int MENU_EXIT_ID = Menu.FIRST + 2;

    protected Saudioserver m_player;
    protected Saudioclient m_recorder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_start = (Button) findViewById(R.id.MENU_START_ID);
        bt_stop = (Button) findViewById(R.id.MENU_STOP_ID);
        bt_exit = (Button) findViewById(R.id.MENU_EXIT_ID);
        bt_exit.setOnClickListener(this);
        bt_start.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MENU_START_ID: {
                m_player = new Saudioserver();
                m_recorder = new Saudioclient();

                m_recorder.start();
                m_player.start();

            }
            break;
            case R.id.MENU_STOP_ID: {
                m_recorder.free();
                m_player.free();
                m_player = null;
                m_recorder = null;
            }
            break;
            case R.id.MENU_EXIT_ID: {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
            break;
            default:
                break;
        }
    }
}

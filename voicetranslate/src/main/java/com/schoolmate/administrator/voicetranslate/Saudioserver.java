package com.schoolmate.administrator.voicetranslate;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class Saudioserver extends Thread {
    protected AudioTrack m_out_trk;
    protected int outBfSize;
    protected byte[] outDataByte;
    protected boolean m_keep_running;
    private ServerSocket Saudioserver;
    private DataInputStream din;

    public void init() {
        try {
            Saudioserver = new ServerSocket(4332);
            Log.i("Saudioserver", "init: Saudioserver创建成功");
            Socket recoredSocket = Saudioserver.accept();
            din = new DataInputStream(recoredSocket.getInputStream());
            m_keep_running = true;

            outBfSize = AudioTrack.getMinBufferSize(8000,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            m_out_trk = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    outBfSize,
                    AudioTrack.MODE_STREAM);

            outDataByte = new byte[outBfSize];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void free() {
        m_keep_running = false;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.d("sleep exceptions...\n", "");
        }
    }

    public void run() {
        init();
        byte[] DataBytes = null;
        m_out_trk.play();
        while (m_keep_running) {
            try {
                din.read(outDataByte);
                DataBytes = outDataByte.clone();
                Log.i("Saudioserver", "长度: " + DataBytes.length);
                m_out_trk.write(DataBytes, 0, DataBytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        m_out_trk.stop();
        m_out_trk = null;
        try {
            din.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


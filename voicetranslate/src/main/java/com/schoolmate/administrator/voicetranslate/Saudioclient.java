package com.schoolmate.administrator.voicetranslate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class Saudioclient extends Thread {

    protected AudioRecord audioRecord;
    protected int byteSize;
    protected byte[] voiceDataByte;
    protected boolean m_keep_running;
    protected Socket s;
    protected DataOutputStream dout;
    protected LinkedList<byte[]> byteLinkList;

    public void run() {
        try {
            init();
            byte[] bytes_pkg;
            audioRecord.startRecording();
            File file = new File(Environment.getExternalStorageDirectory(), "/Music.mp3");
            OutputStream outputStream = new FileOutputStream(file);
            while (m_keep_running) {
                audioRecord.read(voiceDataByte, 0, byteSize);
                bytes_pkg = voiceDataByte.clone();
                outputStream.write(bytes_pkg);
                if (byteLinkList.size() >= 2) {
                    dout.write(byteLinkList.removeFirst(), 0, byteLinkList.removeFirst().length);

                    Log.i("Saudioclient", "run: 写出了一个字节");
                }
                byteLinkList.add(bytes_pkg);
            }

            audioRecord.stop();
            audioRecord = null;
            voiceDataByte = null;
            dout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        byteSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                byteSize);

        voiceDataByte = new byte[byteSize];

        m_keep_running = true;
        byteLinkList = new LinkedList<byte[]>();
        Log.i("Saudioclient", "正在链接");
        try {
            s = new Socket("10.32.129.116", 4332);
            dout = new DataOutputStream(s.getOutputStream());
            Log.i("Saudioclient", "初始化完毕");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
}  

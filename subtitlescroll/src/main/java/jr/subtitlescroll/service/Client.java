package jr.subtitlescroll.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Administrator on 2017/9/29.
 */

public class Client extends Thread {
    private Socket socket;
    RecivedMessage recivedMessage;

    public Client(Socket socket, RecivedMessage recivedMessage) {
        this.socket = socket;
        this.recivedMessage = recivedMessage;
    }

    @Override
    public void run() {
        super.run();
        try {
            socket.setSoTimeout(0);//设置长连接，否则会报Read time out异常
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String msg = br.readLine();
                if (msg != null && msg.equals("")) continue;
                if (msg == null) {
                    try {
                        br.close();
                        socket.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        recivedMessage.disConnect();
                    }
                }
                recivedMessage.recived(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface RecivedMessage {
        void recived(String data);

        void disConnect();
    }
}

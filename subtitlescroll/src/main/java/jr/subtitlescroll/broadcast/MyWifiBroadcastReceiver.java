package jr.subtitlescroll.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

/**
 * Created by Administrator on 2017/10/1.
 */

public class MyWifiBroadcastReceiver extends BroadcastReceiver {
    public WifiStaus wifiStaus;

    public MyWifiBroadcastReceiver(WifiStaus wifiStaus) {
        this.wifiStaus = wifiStaus;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WIFI_STATE_DISABLED);
            if (wifistate == WIFI_STATE_ENABLED) {
                wifiStaus.connect();
                Log.e("MyWifiBroadcastReceiver", "no");
            } else if (wifistate == WIFI_STATE_DISABLED) {
                wifiStaus.disConnect();
                Log.e("MyWifiBroadcastReceiver", "off");
            }
        }
    }

    public interface WifiStaus {
        void connect();

        void disConnect();
    }
}

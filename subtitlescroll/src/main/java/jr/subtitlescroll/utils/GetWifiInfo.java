package jr.subtitlescroll.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * Created by Administrator on 2017/9/29.
 */

public class GetWifiInfo {
    private static WifiManager my_wifiManager;
    private static WifiInfo wifiInfo;
    private static DhcpInfo dhcpInfo;
    private static Context mContext;
    public static GetWifiInfo getWifiInfo;

    private GetWifiInfo() {
        my_wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    public static GetWifiInfo getInstance(Context context) {
        if (getWifiInfo == null) {
            mContext = context;
            getWifiInfo = new GetWifiInfo();
        } else {
            return getWifiInfo;
        }
        return getWifiInfo;
    }

    public HashMap<String, String> getWifiInfo() {
        dhcpInfo = my_wifiManager.getDhcpInfo();
        wifiInfo = my_wifiManager.getConnectionInfo();
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("ipAddress", intToIp(dhcpInfo.ipAddress));
        info.put("netmask", intToIp(dhcpInfo.netmask));
        info.put("gateway", intToIp(dhcpInfo.gateway));
        info.put("serverAddress", intToIp(dhcpInfo.serverAddress));
        info.put("dns1", intToIp(dhcpInfo.dns1));
        info.put("dns2", intToIp(dhcpInfo.dns2));
        info.put("nIpAddress", intToIp(wifiInfo.getIpAddress()));
        info.put("nIpAddress", wifiInfo.getMacAddress());
        return info;
    }

    /**
     * 热点开关是否打开
     *
     * @return
     */
    public boolean isWifiApEnabled() {
        try {
            Method method = my_wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(my_wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }
}

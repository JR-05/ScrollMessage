package jr.subtitlescroll;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/29.
 */

public class MyAppliction extends Application {
    public ArrayList<HashMap<String, Object>> clientList;
    public int clientAccount = 0;
    public String position;
    public float width;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyAppliction", "MyAppOncreate");
        clientList = new ArrayList<HashMap<String, Object>>();
    }

}

package jr.subtitlescroll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import jr.subtitlescroll.R;

/**
 * Created by Administrator on 2017/9/28.
 */

public class IP_Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> clientList;

    public IP_Adapter(Context context, ArrayList<HashMap<String, Object>> clientList) {
        this.mContext = context;
        this.clientList = clientList;
    }

    @Override
    public int getCount() {
        return clientList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_itemview, null);
            viewHolder.postion_tv = (TextView) convertView.findViewById(R.id.adapter_item_position);
            viewHolder.bind_tv = (TextView) convertView.findViewById(R.id.adapter_item_phone);
            viewHolder.ip_tv = (TextView) convertView.findViewById(R.id.adapter_item_ip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> data = clientList.get(position);
        //手机信息
        HashMap<String, String> phoneData = (HashMap<String, String>) data.get("phoneMsg");
        String bind = phoneData.get("bind");
        String ip = phoneData.get("ip");
        viewHolder.postion_tv.setText(String.valueOf(position+1));
        viewHolder.bind_tv.setText(bind);
        viewHolder.ip_tv.setText(ip);
        return convertView;
    }

    class ViewHolder {
        TextView postion_tv;
        TextView bind_tv;
        TextView ip_tv;
    }
}

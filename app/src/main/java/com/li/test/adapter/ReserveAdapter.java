package com.li.test.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.li.test.R;
import com.li.test.bean.OrderInfo;
import com.li.test.util.LogUtil;
import com.li.test.util.OrderType;

import java.util.ArrayList;

/**
 * 订单列表适配器
 */
public class ReserveAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<OrderInfo> list_data;
    private LayoutInflater inflater;

    public ReserveAdapter(Context context, ArrayList<OrderInfo> list_data) {
        this.mcontext = context;
        this.list_data = list_data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.reserve_adapter_layout, null);
            holder = new MyHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.reserve_tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.reserve_tv_phone_number);
            holder.tv_status = (TextView) convertView.findViewById(R.id.reserve_tv_status);
            holder.tv_time = (TextView) convertView.findViewById(R.id.reserve_tv_time);
            holder.order_number = (TextView) convertView.findViewById(R.id.order_number_str);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tv_name.setText(list_data.get(position).UserName);
        holder.tv_phone.setText(list_data.get(position).UserMobile);
        holder.tv_status.setText(list_data.get(position).StatusName);
        holder.tv_time.setText(list_data.get(position).CreateDate);
        holder.order_number.setText(list_data.get(position).OrderNo);
        setStatusColor(list_data.get(position).Status, holder);
        return convertView;
    }

    //改变状态字体的颜色
    private void setStatusColor(int status, MyHolder holder){
        try {
            switch (status) {
                case OrderType.TYPE1:
                    holder.tv_status.setTextColor(ContextCompat.getColor(mcontext, R.color.no_revation_color));
                    break;
                case OrderType.TYPE2:
                    holder.tv_status.setTextColor(ContextCompat.getColor(mcontext, R.color.is_revation_color));
                    break;
                case OrderType.TYPE3:
                    holder.tv_status.setTextColor(ContextCompat.getColor(mcontext, R.color.combo_select_text_color));
                    break;
                default:
                    holder.tv_status.setTextColor(ContextCompat.getColor(mcontext, R.color.combo_select_text_color));
                    break;
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "setStatusColor()", e);
        }
    }

    class MyHolder {
        TextView tv_name;
        TextView tv_phone;
        TextView tv_status;
        TextView tv_time;
        TextView order_number;
    }
}

package android_serialport_api.cardwriteread.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android_serialport_api.cardwriteread.R;

public class OrderDataDialogAdapter extends RecyclerView.Adapter<OrderDataDialogAdapter.ViewHolder> {
    private List<com.alibaba.fastjson.JSONObject> list;

    public OrderDataDialogAdapter(List<com.alibaba.fastjson.JSONObject> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdatadialog_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.orderdatadialogItemT1.setTextColor(Color.parseColor("#000000"));
        holder.orderdatadialogItemT2.setTextColor(Color.parseColor("#000000"));
        holder.orderdatadialogItemT3.setTextColor(Color.parseColor("#000000"));
        if (position == 0) {
            holder.orderdatadialogItemT1.setTextColor(Color.parseColor("#4678ff"));
            holder.orderdatadialogItemT2.setTextColor(Color.parseColor("#4678ff"));
            holder.orderdatadialogItemT3.setTextColor(Color.parseColor("#4678ff"));
            holder.orderdatadialogItemT1.setText("时间");
            holder.orderdatadialogItemT2.setText("订单");
            holder.orderdatadialogItemT3.setText("价格");
            return;
        }
        int type = list.get(position - 1).getInteger("type");
        switch (type) {
            case 1:
                holder.orderdatadialogItemT1.setText("当日");
                break;
            case 2:
                holder.orderdatadialogItemT1.setText("昨日");
                break;
            case 3:
                holder.orderdatadialogItemT1.setText("本周");
                break;
            case 4:
                holder.orderdatadialogItemT1.setText("本月");
                break;
            case 5:
                holder.orderdatadialogItemT1.setText(getPrevMonthData(new Date(), 1));
                break;
            case 6:
                holder.orderdatadialogItemT1.setText(getPrevMonthData(new Date(), 2));
                break;
        }
        holder.orderdatadialogItemT2.setText(list.get(position - 1).getString("count"));
        holder.orderdatadialogItemT3.setText(list.get(position - 1).getString("moneyStr"));
    }

    private String getPrevMonthData(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -n);
        return new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderdatadialogItemT1;
        TextView orderdatadialogItemT2;
        TextView orderdatadialogItemT3;

        public ViewHolder(View itemView) {
            super(itemView);
            orderdatadialogItemT1 = (TextView) itemView.findViewById(R.id.orderdatadialog_item_t1);
            orderdatadialogItemT2 = (TextView) itemView.findViewById(R.id.orderdatadialog_item_t2);
            orderdatadialogItemT3 = (TextView) itemView.findViewById(R.id.orderdatadialog_item_t3);
        }
    }

}

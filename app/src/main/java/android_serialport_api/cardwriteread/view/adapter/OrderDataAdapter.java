package android_serialport_api.cardwriteread.view.adapter;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.net.bean.OrderDetail;
import android_serialport_api.cardwriteread.view.animation.ScaleInAnimation;

public class OrderDataAdapter extends RecyclerView.Adapter<OrderDataAdapter.ViewHolder> {
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private ScaleInAnimation mSelectAnimation = new ScaleInAnimation();

    public OrderDataAdapter(){
        List<OrderDetail> temp = MyApplication.getAppDBManager().getOrderDao().queryOrderList();
        for (OrderDetail o : temp){
            if (o.getShopId().equals(Constant.branchId)){
                orderDetails.add(o);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimation(holder);
    }

    private void addAnimation(ViewHolder holder) {
        for (Animator anim : mSelectAnimation.getAnimators(holder.itemView)) {
            anim.setDuration(300).start();
            anim.setInterpolator(new LinearInterpolator());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_data_adapter, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.orderDataAdapterOrderno.setText("订单号:"+orderDetails.get(position).getOrderNo());
        holder.orderDataAdapterMoney.setText("金额:"+orderDetails.get(position).getPrice());
        if (orderDetails.get(position).getOrderType() == 1){
            holder.orderDataAdapterType.setText("人脸");
        }else if (orderDetails.get(position).getOrderType() == 2){
            holder.orderDataAdapterType.setText("刷卡");
        }else if (orderDetails.get(position).getOrderType() == 4){
            holder.orderDataAdapterType.setText("二维码");
        }else{
            holder.orderDataAdapterType.setText("订单错误");
        }
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderDataAdapterOrderno;
        private TextView orderDataAdapterMoney;
        private TextView orderDataAdapterType;
        public ViewHolder(View itemView) {
            super(itemView);
            orderDataAdapterOrderno = (TextView) itemView.findViewById(R.id.order_data_adapter_orderno);
            orderDataAdapterMoney = (TextView) itemView.findViewById(R.id.order_data_adapter_money);
            orderDataAdapterType = (TextView) itemView.findViewById(R.id.order_data_adapter_type);
        }
    }

}

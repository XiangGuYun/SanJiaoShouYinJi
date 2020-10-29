package android_serialport_api.cardwriteread.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.cardwriteread.R;


public class PromptAdapter extends RecyclerView.Adapter<PromptAdapter.ViewHolder> {
    private List<Integer> list;
    private EditText input;
    private promptItemClick promptItemClick; //点击回调
    public interface promptItemClick{
        void Click(float v);
    }

    public PromptAdapter(EditText input, promptItemClick promptItemClick){
        this.input = input;
        this.promptItemClick = promptItemClick;
        initdata();
    }

    private void initdata(){
        list = new ArrayList<>();
        list.add(5);
        list.add(10);
        list.add(20);
        list.add(50);
        list.add(100);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prompt_item,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String s = input.getText().toString();
        if (!TextUtils.isEmpty(s)){
            //处理字符串转换失败
            try {
                if (Integer.valueOf(s) < list.get(position)){
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setText("￥"+list.get(position));
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击直接拉起支付
                            promptItemClick.Click(list.get(position));
                        }
                    });
                }
            }catch (Exception v1){
                v1.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.prompt_item_b1);
        }
    }

}

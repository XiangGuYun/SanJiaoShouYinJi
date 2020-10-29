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

public class CalculatorNumberAdapter extends RecyclerView.Adapter<CalculatorNumberAdapter.ViewHolder> {
    private List<String> list;
    //传进来键盘高度
    private int height = 0;
    //把输入框传进来
    private EditText input;

    public CalculatorNumberAdapter(int height, EditText editText) {
        this.height = height;
        this.input = editText;
        initList();
    }

    //初始化数据
    private void initList() {
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("0");
        list.add("10");
        list.add("11");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (Integer.valueOf(list.get(position)) == 11) {
            holder.button.setText("删除");
        } else if (Integer.valueOf(list.get(position)) == 10) {
            holder.button.setText(".");
        } else {
            holder.button.setText(list.get(position));
        }
        holder.button.setMinHeight(height / 4);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.button.getText().equals("删除")) {
                    //退格
                    //delctSingle();
                    //清空
                    input.setText("");
                } else {
                    String sin = input.getText().toString() + holder.button.getText();
                    //第一位数不允许是小数点
                    if (TextUtils.isEmpty(input.getText()) && holder.button.getText().equals(".")) {
                        return;
                    }
                    //第一位数如果是0那么后面不允许再输入0了
                    if (!TextUtils.isEmpty(sin) && sin.length() == 2 && sin.substring(0,1).equals("0") && sin.substring(1,2).equals("0")){
                        return;
                    }
                    //小数点只允许存在一个
                    if (checkNumberPoint() && holder.button.getText().equals(".")) return;
                    //小数点后最多俩位
                    if (sin.indexOf(".") != -1) {
                        //存在小数点 检查是否是俩位数
                        if (sin.split("\\.").length > 1){
                            String s = sin.split("\\.")[1];
                            if (s.length() > 2) { // 是俩位数了不允许再输入
                                return;
                            }
                        }
                    }
                    //最大限额一万
                    if (!TextUtils.isEmpty(sin) && sin.indexOf(".") == -1){
                        if (Integer.parseInt(sin) > 9999){
                            return;
                        }
                    }else if (sin.indexOf(".") != -1){
                        if (Integer.parseInt(sin.split("\\.")[0]) > 9999){
                            return;
                        }
                    }
                    input.append(holder.button.getText());
                }
            }
        });
    }

    private boolean checkNumberPoint() {
        int count = 0;
        String s = input.getText().toString();
        for (int i = 0; i < s.length(); i++) {
            if (String.valueOf(s.charAt(i)).equals(".")) {
                count++;
            }
        }
        return count >= 1 ? true : false;
    }

    //删除一个char
    private void delctSingle() {
        String s = input.getText().toString();
        if (s.length() > 0) {
            input.setText(s.substring(0, s.length() - 1));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.number_item_b1);
        }
    }

}

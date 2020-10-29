package android_serialport_api.cardwriteread.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import android_serialport_api.cardwriteread.R;

public class SubbranchAdapter extends RecyclerView.Adapter<SubbranchAdapter.ViewHolder> {
    private List<JSONObject> list;
    private ItemClick itemClick; //点击回调
    private Activity activity;

    public interface ItemClick {
        void Click(JSONObject object) throws JSONException;
    }

    public SubbranchAdapter(List<JSONObject> list, ItemClick itemClick, Activity activity) {
        this.list = list;
        this.itemClick = itemClick;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subbranch_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.t1.setText(list.get(position).getString("branchId"));
            holder.t2.setText(list.get(position).getString("branchName"));
            int w = activity.getWindowManager().getDefaultDisplay().getWidth();
            int h = activity.getWindowManager().getDefaultDisplay().getHeight();
            holder.layout.setMinimumWidth(w > h ? w : h);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        itemClick.Click(list.get(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView t1;
        TextView t2;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.subbranch_item_group);
            t1 = itemView.findViewById(R.id.subbranch_item_t1);
            t2 = itemView.findViewById(R.id.subbranch_item_t2);
        }
    }

}

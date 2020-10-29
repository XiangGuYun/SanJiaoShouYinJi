package android_serialport_api.cardwriteread.meal;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import java.util.Arrays;
import java.util.List;

import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.view.MainActivity;

/**
 * 管理所有的列表
 */
public class RV {

    /**
     * 处理菜品乘以数字列表
     * @param rvX
     */
    public static void doXNumberList(RecyclerView rvX){
        final List<String> xList = Arrays.asList("X1","X2","X3","X4","X5","X6","X7","X8","X9","X10");

        new RVUtils(rvX).adapter(xList, (holder, pos) -> {
            holder.setText(R.id.tvX, xList.get(pos));
            if(pos == MainActivity.selectedIndex){
                holder.getView(R.id.tvX).setBackground(rvX.getContext().getResources().getDrawable(R.drawable.x_number));
            } else {
                holder.getView(R.id.tvX).setBackground(null);
            }
            holder.setOnItemViewClickListener(v -> {
                MainActivity.selectedIndex = pos;
                rvX.getAdapter().notifyDataSetChanged();
            });
        }, position -> 0, R.layout.item_x_number);
    }

    /**
     * 处理左侧已购买的菜品列表
     */
    public static void doLeftBuyDishList(){

    }

    public static void update(ListView listView){
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    public static void update(GridView listView){
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

}

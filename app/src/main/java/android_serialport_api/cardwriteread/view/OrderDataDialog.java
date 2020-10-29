package android_serialport_api.cardwriteread.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.apache.commons.codec.binary.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.util.MethodUtils;
import android_serialport_api.cardwriteread.view.adapter.OrderDataDialogAdapter;

/**
 * 订单数据显示弹窗
 */

public class OrderDataDialog extends Dialog {
    private Activity activity;
    private List<JSONObject> list;
    private RecyclerView orderdatadialogRecy;
    private BarChart mChart;
    private Button orderdatadialogClose;

    public OrderDataDialog(@NonNull Activity activity, List<JSONObject> list) {
        super(activity, R.style.MyDialog);
        this.activity = activity;
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdatadialog);
        initView();
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.height = (int) MethodUtils.getInstanceSingle().getDimens(activity, R.dimen.dp_200);
        params.width = (int) MethodUtils.getInstanceSingle().getDimens(activity, R.dimen.dp_410);
        this.getWindow().setAttributes(params);
    }

    private void initView() {
        orderdatadialogClose = findViewById(R.id.orderdatadialog_close);
        orderdatadialogRecy = (RecyclerView) findViewById(R.id.orderdatadialog_recy);
        mChart = (BarChart) findViewById(R.id.orderdatadialog_chart);
        OrderDataDialogAdapter adapter = new OrderDataDialogAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        orderdatadialogRecy.setLayoutManager(linearLayoutManager);
        orderdatadialogRecy.setAdapter(adapter);
        initBarChart();
        orderdatadialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    /**
     * 初始化柱形图控件属性
     */
    private void initBarChart() {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);

        //获取到图形左边的Y轴
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        //获取到图形右边的Y轴，并设置为不显示
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);

        //图例设置
        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);
        mChart.animateXY(700, 700);
        setBarChartData();
    }

    private void setBarChartData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        final List<String> xValus = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            yVals1.add(new BarEntry(i, Float.parseFloat(list.get(i).getString("count"))));
            yVals2.add(new BarEntry(i, Float.parseFloat(list.get(i).getString("moneyStr").replace(",", ""))));
            int type = list.get(i).getInteger("type");
            String s = "";
            switch (type) {
                case 1:
                    s = "当日";
                    break;
                case 2:
                    s = "昨日";
                    break;
                case 3:
                    s = "本周";
                    break;
                case 4:
                    s = "本月";
                    break;
                case 5:
                    s = getPrevMonthData(new Date(), 1);
                    break;
                case 6:
                    s = getPrevMonthData(new Date(), 2);
                    break;
            }
            xValus.add(s);
        }

        BarDataSet set1;
        BarDataSet set2;

        set1 = new BarDataSet(yVals1, "总订单");
        set1.setDrawIcons(false);
        set2 = new BarDataSet(yVals2, "总金额");
        set2.setDrawIcons(false);

        set1.setColor(Color.rgb(129, 216, 200));
        set2.setColor(Color.rgb(181, 194, 202));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValus.get(((int) value));
            }
        });
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        float groupSpace = 0.1f; //柱状图组之间的间距
        float barWidth = (1f - 0.1f) / 2.3f;
        float barSpace = 0f;
        //设置柱状图宽度
        data.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0f, groupSpace, barSpace);

        mChart.setData(data);
    }

    private String getPrevMonthData(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -n);
        return new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
    }

}

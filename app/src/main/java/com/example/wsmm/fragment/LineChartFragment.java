package com.example.wsmm.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.example.wsmm.R;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.LineChartCategoryModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmPieData;
import com.github.mikephil.charting.data.realm.implementation.RealmPieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by abubaker on 15/05/2016.
 */
public class LineChartFragment extends BaseFragment{


    private LineChart mChart;
    private Typeface mTf;
    DBClient dbClient;
    ArrayList<LineChartCategoryModel> modelList;
    List<Category> result;


    public LineChartFragment(){

    }


    @Override
    public int getLayoutId() {
        return R.layout.line_chart_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);

        mChart = (LineChart) parent.findViewById(R.id.line_chart);

        setup(mChart);
    }


    protected void setup(Chart<?> chart) {

        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        // no description text
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        chart.setTouchEnabled(true);

        if (chart instanceof BarLineChartBase) {

            BarLineChartBase mChart = (BarLineChartBase) chart;

            mChart.setDrawGridBackground(false);

            // enable scaling and dragging
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.setTypeface(mTf);
            leftAxis.setTextSize(8f);
            leftAxis.setTextColor(Color.DKGRAY);
            leftAxis.setValueFormatter(new PercentFormatter());

            XAxis xAxis = mChart.getXAxis();
            xAxis.setTypeface(mTf);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setTextColor(Color.DKGRAY);

            mChart.getAxisRight().setEnabled(false);
        }
    }


    protected void styleData(ChartData data) {
        data.setValueTypeface(mTf);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {

        dbClient =new DBClient();

        if (result == null){

            result = dbClient.getAllItems();

        }


        for (int i = 0;i<result.size();i++){
            LineChartCategoryModel model = new LineChartCategoryModel();
            model.setId(result.get(i).getCategoryId()-1);
            model.setPrice(Float.parseFloat(result.get(i).getPrice()));
            model.setDate(result.get(i).getStringDate());
            dbClient.saveChartData(model);
        }

        RealmResults<LineChartCategoryModel> chartData = dbClient.getChartData();

        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
        RealmLineDataSet<LineChartCategoryModel> set = new RealmLineDataSet<LineChartCategoryModel>(chartData, "price", "categoryChartId"); // stacked entries
        set.setDrawCubic(false);
        set.setLabel("Realm LineDataSet");
        set.setDrawCircleHole(false);
        set.setColor(ColorTemplate.rgb("#FF5722"));
        set.setCircleColor(ColorTemplate.rgb("#FF5722"));
        set.setLineWidth(1.8f);
        set.setCircleSize(3.6f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set); // add the dataset

        // create a data object with the dataset list
        RealmLineData data = new RealmLineData(chartData, "date", dataSets);
        styleData(data);

        // set data
        mChart.setData(data);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);;
    }


    public void setRealmList(List<Category> dataSet) {

        result = dataSet;

    }

    private List<Category> getDataSet() {
        return result;
    }
}

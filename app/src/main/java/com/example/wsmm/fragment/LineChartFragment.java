package com.example.wsmm.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.LineChartCategoryModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    TextView message;
    private HashMap<Integer,String> hashMap;
    private int counter = -1;


    public LineChartFragment(){

    }


    @Override
    public int getLayoutId() {
        return R.layout.line_chart_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        dbClient =new DBClient();

        mChart = (LineChart) parent.findViewById(R.id.line_chart);
        message = (TextView)parent.findViewById(R.id.text_message);

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
        if (dbClient.getChartData()!=null){
            dbClient.clearChartData();
        }

        setData();
    }

    private void setData() {

        if (hashMap!= null && hashMap.size() > 0){



            for (int i = 0;i<result.size();i++){
                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    if (pair.getValue().equals(result.get(i).getCategoryName().toUpperCase())){
                        LineChartCategoryModel model = new LineChartCategoryModel();
                        model.setId(++counter);
                        model.setCatName(result.get(i).getCategoryTitle());
                        model.setPrice(Float.parseFloat(result.get(i).getPrice()));
                        model.setDate(result.get(i).getStringDate());
                        dbClient.saveChartData(model);

                    }


                }

            }

        }else {

            for (int i = 0;i<result.size();i++){
                LineChartCategoryModel model = new LineChartCategoryModel();
                model.setId(i);
                model.setPrice(Float.parseFloat(result.get(i).getPrice()));
                model.setDate(result.get(i).getStringDate());
                dbClient.saveChartData(model);
            }


        }


        RealmResults<LineChartCategoryModel> chartData = dbClient.getChartData();


        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
        RealmLineDataSet<LineChartCategoryModel> set = new RealmLineDataSet<LineChartCategoryModel>(chartData, "price", "categoryChartId"); // stacked entries
        set.setDrawCubic(false);
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

        if (chartData.size() < 1){
            message.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
        }

        // set data
        mChart.setData(data);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
       // mChart.getDefaultValueFormatter();



    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void setRealmList(List<Category> dataSet) {

        result = dataSet;

    }

    private List<Category> getDataSet() {
        return result;
    }

    public void setSelectCategories(HashMap<Integer,String> hashMap){
        this.hashMap = hashMap;

    }
}

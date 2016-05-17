package com.example.wsmm.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.realm.implementation.RealmPieData;
import com.github.mikephil.charting.data.realm.implementation.RealmPieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Calendar;

import io.realm.RealmResults;

/**
 * Created by abubaker on 5/13/16.
 */
public class PieFragment extends BaseFragment {

    private PieChart mChart;
    private Typeface mTf;
    DBClient dbClient;
    RealmResults<Category> result;
    RealmPieData data;
    RealmPieDataSet<Category> set;
    TextView message;


    public PieFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.pie_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);

        mChart = (PieChart) parent.findViewById(R.id.pie_chart);
        message = (TextView)parent.findViewById(R.id.text_message);

        setup(mChart);

        //mChart.setCenterText(generateCenterSpannableText());


    }


    @Override
    public void onResume() {
        super.onResume();
        setData();
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


    private void setData() {

        if (result == null) {

            dbClient = new DBClient();

            Calendar now = Calendar.getInstance();

           result = dbClient.getParticularRealmResult(now.get(Calendar.DAY_OF_MONTH),now.get(Calendar.MONTH)+1,
                   now.get(Calendar
                   .YEAR));


        }


        RealmPieDataSet<Category> set = new RealmPieDataSet<Category>(result, "expensePrice", "categoryId"); // stacked entries
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setLabel("Example market share");
        set.setSliceSpace(2);


        data = new RealmPieData(result, "categoryName", set);


        styleData(data);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(10f);

        // set data
        mChart.setUsePercentValues(true);
        mChart.setData(data);
        mChart.animateY(1400);

        if (result.size() < 1){
            message.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
        }
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Realm.io\nmobile database");
        s.setSpan(new ForegroundColorSpan(Color.rgb(240, 115, 126)), 0, 8, 0);
        s.setSpan(new RelativeSizeSpan(2.2f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 9, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.85f), 9, s.length(), 0);
        return s;
    }

    public void setRealmDataSet(RealmResults<Category> dataSet) {

        result = dataSet;

    }

    private RealmResults<Category> getDataSet() {
        return result;
    }
}

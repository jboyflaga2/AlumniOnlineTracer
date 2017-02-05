package example.com.sampleproject;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.sampleproject.core.DaggerService;
import example.com.sampleproject.core.MainComponent;

/**
 * Created by User-01 on 1/9/2017.
 */

public class ChartView extends LinearLayout {

    @Inject
    ChartScreen.Presenter presenter;

    @BindView(R.id.piechart)
    PieChart pieChart;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<MainComponent>getDaggerComponent(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

    public void showChart(int nEmployed, int nNotEmployed) {
        List<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry1 = new PieEntry(nNotEmployed/(float)100.0, "Not Employed");
        pieEntries.add(pieEntry1);
        pieEntries.add(new PieEntry(nEmployed/(float)100.0, "Employed"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Pie Entries"); // add entries to dataset
        pieDataSet.setValueTextColor(Color.WHITE); // styling, ...

        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);

        pieChart.invalidate();
    }
}

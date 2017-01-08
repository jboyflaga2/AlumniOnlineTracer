package example.com.sampleproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.sampleproject.core.DaggerService;
import example.com.sampleproject.core.MainComponent;
import example.com.sampleproject.models.PersonalInfo;
import flow.Flow;

//import example.com.sampleproject.core.DaggerMainComponent;

/**
 * Created by User-01 on 12/24/2016.
 */

public class EmploymentView extends LinearLayout {

    @Inject
    EmploymentScreen.Presenter presenter;

    @BindView(R.id.employed_radiogroup)
    RadioGroup employedRadioGroup;

    @BindView(R.id.employed_yes_radiobutton)
    RadioButton employedYesRadioButton;

    @BindView(R.id.employed_no_radiobutton)
    RadioButton employedNoRadioButton;
//
//    @BindView(R.id.linechart)
//    LineChart lineChart;

    @BindView(R.id.piechart)
    PieChart pieChart;

    public EmploymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //DaggerService.<DaggerPersonalInfoScreen.Component>getDaggerComponent(context).inject(this);
        DaggerService.<MainComponent>getDaggerComponent(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

//        List<Entry> entries = new ArrayList<Entry>();
//        entries.add(new Entry(10, 10));
//        entries.add(new Entry(5, 5));
//        entries.add(new Entry(10, 5));
//        entries.add(new Entry(5, 10));
//        entries.add(new Entry(7, 9));
//        entries.add(new Entry(30, 15));
//
//        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColor(Color.RED);
//        dataSet.setValueTextColor(Color.BLUE); // styling, ...
//
//        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);
//        lineChart.invalidate(); // refresh


        List<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry1 = new PieEntry(10, "Not Employed");
        pieEntries.add(pieEntry1);
        pieEntries.add(new PieEntry(90, "Employed"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Pie Entries"); // add entries to dataset
        pieDataSet.setValueTextColor(Color.BLACK); // styling, ...

        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);

        pieChart.invalidate();
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

    @OnClick(R.id.submit_button)
    protected void onSubmitButtonClick() {

        int selectedEmployedRadioButtonId = employedRadioGroup.getCheckedRadioButtonId();

        String userId = "";
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", 0);
        userId = prefs.getString("USER_ID", "");

        PersonalInfo employmentInfo = new PersonalInfo();
        employmentInfo.setUserId(userId);
        employmentInfo.setEmployed(selectedEmployedRadioButtonId == R.id.employed_yes_radiobutton);

        presenter.updateEmploymentInfo(employmentInfo);

    }

    @OnClick(R.id.back_button)
    protected void onBackButtonClick() {
        Flow.get(EmploymentView.this).goBack();
    }


    public void fillInputs(PersonalInfo employementInfo) {
        if (employementInfo.isEmployed()) {
            employedYesRadioButton.setChecked(true);
        } else {
            employedNoRadioButton.setChecked(true);
        }
    }

}

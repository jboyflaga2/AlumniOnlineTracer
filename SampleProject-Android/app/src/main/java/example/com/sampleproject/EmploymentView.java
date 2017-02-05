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

    public EmploymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //DaggerService.<DaggerPersonalInfoScreen.Component>getDaggerComponent(context).inject(this);
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

    @OnClick(R.id.view_chart_button)
    protected void onViewChartButtonClick() {
        goToViewChart();
    }

    public void fillInputs(PersonalInfo employementInfo) {
        if (employementInfo.isEmployed()) {
            employedYesRadioButton.setChecked(true);
        } else {
            employedNoRadioButton.setChecked(true);
        }
    }

    public void goToViewChart() {
        Flow.get(EmploymentView.this).set(new ChartScreen());
    }

}

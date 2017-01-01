package example.com.sampleproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
//import example.com.sampleproject.core.DaggerMainComponent;
import example.com.sampleproject.core.DaggerService;
import example.com.sampleproject.core.MainComponent;
import flow.Flow;

/**
 * Created by User-01 on 12/24/2016.
 */

public class EmploymentView extends LinearLayout {

    @Inject
    EmploymentScreen.Presenter presenter;

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

    @OnClick(R.id.back_button)
    protected void onBackButtonClick() {
        Flow.get(EmploymentView.this).goBack();
    }
}

package example.com.sampleproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.AccessToken;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.sampleproject.core.DaggerService;
import example.com.sampleproject.core.MainComponent;
import example.com.sampleproject.models.PersonalInfo;
import flow.Flow;

/**
 * Created by User-01 on 12/24/2016.
 */

public class PersonalInfoView extends LinearLayout {

    @Inject
    PersonalInfoScreen.Presenter presenter;

    @BindView(R.id.firstname_edittext)
    EditText firstNameEditText;

    @BindView(R.id.lastname_edittext)
    EditText lastNameEditText;

    @BindView(R.id.email_edittext)
    EditText emailEditText;

    public PersonalInfoView(Context context, AttributeSet attrs) {
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

    @OnClick(R.id.next_button)
    protected void onNextButtonClick() {
        PersonalInfo personalInfoToUpdate = presenter.CurrentPersonalInfo;
        String userId = "";
        //if (isLoggedIn()) {
            SharedPreferences prefs = getContext().getSharedPreferences("PREFS", 0);
            userId = prefs.getString("USER_ID", "");
        //}

        personalInfoToUpdate.setUserId(userId);
        personalInfoToUpdate.setFirstName(firstNameEditText.getText().toString());
        personalInfoToUpdate.setLastName(lastNameEditText.getText().toString());
        personalInfoToUpdate.setEmail(emailEditText.getText().toString());

        presenter.updatePersonalInfo();

        Flow.get(PersonalInfoView.this).set(new EmploymentScreen());
    }

    @OnClick(R.id.back_button)
    protected void onBackButtonClick() {
        Flow.get(PersonalInfoView.this).goBack();
    }

    public void fillInputs(PersonalInfo personalInfo) {
        firstNameEditText.setText(personalInfo.getFirstName());
        lastNameEditText.setText(personalInfo.getLastName());
        emailEditText.setText(personalInfo.getEmail());
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}

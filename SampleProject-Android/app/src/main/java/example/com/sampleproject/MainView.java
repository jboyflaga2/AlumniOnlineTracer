/*
 * Copyright 2014 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.com.sampleproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.sampleproject.core.DaggerService;
import example.com.sampleproject.core.MainComponent;
import flow.Flow;

public class MainView extends LinearLayout {
    @Inject
    MainScreen.Presenter presenter;

    @Inject
    CallbackManager callbackManager;


    @BindView(R.id.personal_info_button)
    Button personalInfoButton;

    @BindView(R.id.facebook_login_button)
    Button facebookLoginButton;

    @BindView(R.id.facebook_logout_button)
    Button facebookLogoutButton;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //DaggerService.<MainScreen.Component>getDaggerComponent(context).inject(this);
        DaggerService.<MainComponent>getDaggerComponent(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        updateFacebookButtons();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return Flow.get(MainView.this).goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.personal_info_button)
    protected void onPersonalInfoButtonClick() {
        goToPersonalInfoView();
    }

    @OnClick(R.id.get_contact_button)
    protected void onGetContactButtonClick() {
        presenter.getContact();
    }

    @OnClick(R.id.facebook_login_button)
    protected void onFacebookLoginButtonClick() {
        presenter.loginUsingFacebook();
    }

    @OnClick(R.id.facebook_logout_button)
    protected void onFacebookLogoutButtonClick() {
        presenter.logoutFromFacebook();
    }

    public void goToPersonalInfoView() {
        Flow.get(MainView.this).set(new PersonalInfoScreen());
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateFacebookButtons() {
        if (presenter.isNotLoggedInUsingFacebook()) {
            facebookLoginButton.setVisibility(View.VISIBLE);
            facebookLogoutButton.setVisibility(View.GONE);
            personalInfoButton.setVisibility(View.GONE);
        } else {
            facebookLoginButton.setVisibility(View.GONE);
            facebookLogoutButton.setVisibility(View.VISIBLE);
            personalInfoButton.setVisibility(View.VISIBLE);
        }
    }
}

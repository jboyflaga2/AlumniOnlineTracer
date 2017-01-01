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
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.List;

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

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //DaggerService.<MainScreen.Component>getDaggerComponent(context).inject(this);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Flow.get(MainView.this).goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.personal_info_button)
    protected void onPersonalInfoButtonClick() {
        Flow.get(MainView.this).set(new PersonalInfoScreen());
    }


    @OnClick(R.id.facebook_login_button)
    protected void onFacebookLoginButtonClick() {
        presenter.goToFacebookLogin();
    }
}

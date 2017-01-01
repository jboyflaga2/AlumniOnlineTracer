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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.com.sampleproject.core.ActivityResultPresenter;
import example.com.sampleproject.core.Layout;
import example.com.sampleproject.core.MainComponent;
import example.com.sampleproject.core.MainModule;
import example.com.sampleproject.core.WindowOwnerPresenter;
import example.com.sampleproject.services.ValuesService;
import flow.Flow;
import mortar.MortarScope;
import mortar.ViewPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Layout(R.layout.main_view)
public class MainScreen {
//
//    @dagger.Component(dependencies = MainComponent.class, modules = MainModule.class)
//    @Singleton
//    interface Component {
//        void inject(MainView t);
//    }

    @Singleton
    static class Presenter extends ViewPresenter<MainView>{

        @Inject
        Presenter(){
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        public void goToFacebookLogin() {
            Intent intent = new Intent(getView().getContext(), FacebookLoginActivity.class);
            getView().getContext().startActivity(intent);
        }
    }
}

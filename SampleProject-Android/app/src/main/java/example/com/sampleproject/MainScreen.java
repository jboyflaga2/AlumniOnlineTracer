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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.com.sampleproject.core.ActivityResultPresenter;
import example.com.sampleproject.core.ActivityResultRegistrar;
import example.com.sampleproject.core.Layout;
import example.com.sampleproject.core.WindowOwnerPresenter;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Layout(R.layout.main_view)
public class MainScreen {
//
//    @dagger.Component(dependencies = MainComponent.class, modules = MainModule.class)
//    @Singleton
//    interface Component {
//        void inject(MainView t);
//    }

    @Singleton
    static class Presenter extends ViewPresenter<MainView>  implements ActivityResultPresenter.ActivityResultListener, FacebookCallback<LoginResult> {

        private CallbackManager callbackManager;
        private List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday");

        private final WindowOwnerPresenter windowOwnerPresenter;
        private final ActivityResultRegistrar activityResultRegistrar;

        @Inject
        Presenter(WindowOwnerPresenter windowOwnerPresenter, ActivityResultRegistrar activityResultRegistrar){
            this.windowOwnerPresenter = windowOwnerPresenter;
            this.activityResultRegistrar = activityResultRegistrar;
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            activityResultRegistrar.register(scope, this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager, this);
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        public void getContact() {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            windowOwnerPresenter.getActivity().startActivityForResult(intent, 1001);
        }

        public void loginUsingFacebook() {
            if(isNotLoggedInUsingFacebook()) {
                LoginManager.getInstance().logInWithReadPermissions(windowOwnerPresenter.getActivity(), permissionNeeds);
            }
        }

        public void logoutFromFacebook() {
            LoginManager.getInstance().logOut();
            getView().updateFacebookButtons();

            //todo: reset user id in shared prefs
        }

        public boolean isNotLoggedInUsingFacebook() {
            return AccessToken.getCurrentAccessToken() == null;
        }

        //region "ActivityResultPresenter.ActivityResultListener"
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case 1001:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri contactData = data.getData();
                        Cursor c =  getView().getContext().getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {
                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            // TODO Whatever you want to do with the selected contact name.
                            getView().showToast("Contact Name: " + name);
                        }
                    }
                    break;
            }
        }
        //endregion

        //region "FacebookCallback"
        @Override
        public void onSuccess(LoginResult loginResult) {
            Toast.makeText(windowOwnerPresenter.getActivity(), "Facebook login successful.", Toast.LENGTH_LONG).show();

            SharedPreferences prefs = windowOwnerPresenter.getActivity().getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USER_ID", loginResult.getAccessToken().getUserId());
            editor.commit();

            getView().goToPersonalInfoView();
        }

        @Override
        public void onCancel() {
            Toast.makeText(windowOwnerPresenter.getActivity(), "Facebook login cancelled.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(windowOwnerPresenter.getActivity(), "Facebook login error.", Toast.LENGTH_LONG).show();

            SharedPreferences prefs = windowOwnerPresenter.getActivity().getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USER_ID", "");
            editor.commit();

            Toast toast = Toast.makeText(windowOwnerPresenter.getActivity(), "Error logging in using Facebook", Toast.LENGTH_LONG);
            toast.show();
        }
        //endregion
    }
}

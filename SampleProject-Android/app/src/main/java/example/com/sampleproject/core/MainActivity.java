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
package example.com.sampleproject.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.util.Map;

import javax.inject.Inject;

import example.com.sampleproject.MainScreen;
import example.com.sampleproject.ChartScreen;
import example.com.sampleproject.PersonalInfoScreen;
import flow.Direction;
import flow.Flow;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.TraversalCallback;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import rx.functions.Action0;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

public class MainActivity extends AppCompatActivity
        implements WindowOwnerPresenter.View,
        ActivityResultPresenter.View,
        ActionBarOwner.Activity {

    private MortarScope activityScope;
    private ActionBarOwner.MenuAction actionBarMenuAction;

    @Inject
    ActivityResultPresenter activityResultPresenter;

    @Inject
    ActionBarOwner actionBarOwner;

    @Override
    protected void attachBaseContext(Context baseContext) {
        baseContext = Flow.configure(baseContext, this)
                .dispatcher(KeyDispatcher.configure(this, new Changer()).build())
                .defaultKey(new MainScreen())
                //.defaultKey(new PersonalInfoScreen())
                .install();

        super.attachBaseContext(baseContext);
    }

    @Override
    public Object getSystemService(String name) {
        activityScope = findChild(getApplicationContext(), getScopeName());
        MainModule mainModule = new MainModule(this, this, this);
        MainComponent mainComponent = DaggerMainComponent.builder().mainModule(mainModule).build();

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, mainComponent)
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    /** Configure the action bar menu as required by {@link ActionBarOwner.Activity}. */
    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        if (actionBarMenuAction != null) {
//            menu.add(actionBarMenuAction.title)
//                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
//                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override public boolean onMenuItemClick(MenuItem menuItem) {
//                            actionBarMenuAction.action.call();
//                            return true;
//                        }
//                    });
//        }
//        menu.add("Say Hi")
//                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override public boolean onMenuItemClick(MenuItem item) {
//                        //Log.d("DemoActivity", MortarScopeDevHelper.scopeHierarchyToString(activityScope));
//                        Toast.makeText(getContext(), "Hi", Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                });
        return true;
    }

    //region "Activity Life Cycle Overrides"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        DaggerService.<MainComponent>getDaggerComponent(this).inject(this);

        FacebookSdk.sdkInitialize(getApplicationContext());

//        setHomeButtonEnabled
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!Flow.get(this).goBack()) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default:
                activityResultPresenter.onActivityResultReceived(requestCode, resultCode, data);
        }
    }
    //endregion

    //region "Implementation of interfaces
    @Override
    public MortarScope getMortarScope() {
        return activityScope;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void startNewActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void startNewActivityForResult(int requestCode, Intent intent) {
        this.startActivityForResult(intent, requestCode);
    }
    //endregion

    //region "Implementation of ActionBarOwner.Activity"

    @Override
    public void setShowHomeEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(enabled);
    }

    @Override
    public void setUpButtonEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enabled);
        actionBar.setHomeButtonEnabled(enabled);
    }

    @Override
    public void setMenu(ActionBarOwner.MenuAction action) {
        if (action != actionBarMenuAction) {
            actionBarMenuAction = action;
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
    //endregion

    //region "Helpers"
    private String getScopeName() {
        return getClass().getName();
    }

    //endregion

    private final class Changer implements KeyChanger {

        @Override
        public void changeKey(@Nullable State outgoingState, @NonNull State incomingState,
                              @NonNull Direction direction, @NonNull Map<Object, Context> incomingContexts,
                              @NonNull TraversalCallback callback) {

            Object key = incomingState.getKey();
            Context context = incomingContexts.get(key);

            if (outgoingState != null) {
                ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
                outgoingState.save(view.getChildAt(0));
            }

            View view;
            if (key.getClass().isAnnotationPresent(Layout.class)) {
                int layoutId = key.getClass().getAnnotation(Layout.class).value();
                view = showLayout(context, layoutId);

//            if (key instanceof WelcomeScreen) {
//                view = showKeyAsText(context, key, new ListContactsScreen());
//            } else if (key instanceof ListContactsScreen) {
//                view = showLayout(context, R.layout.list_contacts_screen);
//            } else if (key instanceof EditNameScreen) {
//                view = showLayout(context, R.layout.edit_name_screen);
//            } else if (key instanceof EditEmailScreen) {
//                view = showLayout(context, R.layout.edit_email_screen);

//            } else if (key instanceof FacebookLoginActivity) {
//                return;
            } else {
                view = showKeyAsText(context, key, null);
            }
            incomingState.restore(view);
            setContentView(view);
            callback.onTraversalCompleted();
        }

        private View showLayout(Context context, @LayoutRes int layout) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(layout, null);
        }

        private View showKeyAsText(Context context, Object key, @Nullable final Object nextScreenOnClick) {
            TextView view = new TextView(context);
            view.setText(key.toString());

            if (nextScreenOnClick == null) {
                view.setOnClickListener(null);
            } else {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Flow.get(v).set(nextScreenOnClick);
                    }
                });
            }
            return view;
        }
    }
}

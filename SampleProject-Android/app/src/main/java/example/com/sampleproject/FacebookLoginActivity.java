package example.com.sampleproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by User-01 on 12/26/2016.
 */

public class FacebookLoginActivity extends AppCompatActivity {

    @BindView(R.id.login_button)
    protected LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        ButterKnife.bind(this);

        callbackManager = CallbackManager.Factory.create();

        final AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    SharedPreferences prefs = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("USER_ID", "");
                    editor.commit();
                }
            }
        };

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessTokenTracker.startTracking();

                SharedPreferences prefs = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_ID", loginResult.getAccessToken().getUserId());
                editor.commit();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                SharedPreferences prefs = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_ID", "");
                editor.commit();

                Toast toast = Toast.makeText(FacebookLoginActivity.this, "Error logging in using Facebook", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //loginButton.performClick();
        //this.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.back_button)
    protected void onBackButtonClick() {
        finish();
    }
}

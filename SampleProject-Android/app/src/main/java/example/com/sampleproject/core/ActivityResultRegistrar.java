package example.com.sampleproject.core;

import android.content.Intent;

import mortar.MortarScope;

public interface ActivityResultRegistrar {

    void register(MortarScope scope, ActivityResultPresenter.ActivityResultListener listener);

    void startActivityForResult(int requestCode, Intent intent);

    void startActivity(Intent intent);
}

package example.com.sampleproject.core;

import com.facebook.CallbackManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.com.sampleproject.services.PersonalInfoService;
import example.com.sampleproject.services.ValuesService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by User-01 on 12/24/2016.
 */

@Module
public class MainModule {

    private final static String BASE_URL = "http://127.0.0.1:5000";

    private final WindowOwnerPresenter.View windowOwnerPresenterView;
    private final  ActivityResultPresenter.View activityResultPresenterView;
    private final ActionBarOwner.Activity actionBarOwnerActivity;

    public MainModule(WindowOwnerPresenter.View windowOwnerPresenterView,
                      ActivityResultPresenter.View activityResultPresenterView,
                      ActionBarOwner.Activity actionBarOwnerActivity) {
        this.windowOwnerPresenterView = windowOwnerPresenterView;
        this.activityResultPresenterView = activityResultPresenterView;
        this.actionBarOwnerActivity = actionBarOwnerActivity;
    }

    @Provides
    @Singleton
    static ValuesService provideValuesSerice() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ValuesService service = retrofit.create(ValuesService.class);
        return service;

        //return  new ValuesService();
    }

    @Provides
    @Singleton
    static PersonalInfoService providePersonalInfoSerice() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        PersonalInfoService service = retrofit.create(PersonalInfoService.class);
        return service;

        //return  new ValuesService();
    }

    private static CallbackManager callbackManager;
    @Provides
    @Singleton
    static CallbackManager provideCallbackManager() {
        if (callbackManager == null) {
            callbackManager = CallbackManager.Factory.create();
        }
        return callbackManager;
    }

    @Provides
    @Singleton
    WindowOwnerPresenter provideWindowOwnerPresenter() {
        WindowOwnerPresenter windowOwnerPresenter = new WindowOwnerPresenter();
        windowOwnerPresenter.takeView(windowOwnerPresenterView);
        return windowOwnerPresenter;
    }

    @Provides
    @Singleton
    ActivityResultPresenter provideActivityResultPresenter() {
        ActivityResultPresenter activityResultPresenter = new ActivityResultPresenter();
        activityResultPresenter.takeView(activityResultPresenterView);
        return activityResultPresenter;
    }

    @Provides
    @Singleton
    static ActivityResultRegistrar provideIntentLauncher(ActivityResultPresenter presenter) {
        return presenter;
    }

    @Provides
    @Singleton
    ActionBarOwner provideActionBarOwner() {
        ActionBarOwner actionBarOwner = new ActionBarOwner();
        actionBarOwner.takeView(actionBarOwnerActivity);
        return actionBarOwner;
    }
}

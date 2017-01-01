package example.com.sampleproject.core;

import com.facebook.CallbackManager;

import java.util.List;

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

    private WindowOwnerPresenter.View windowOwnerPresenterView;

    public MainModule(WindowOwnerPresenter.View windowOwnerPresenterView) {
        this.windowOwnerPresenterView = windowOwnerPresenterView;
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
                //.addConverterFactory(ScalarsConverterFactory.create())
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
    static ActivityResultPresenter provideActivityResultPresenter() {
        return new ActivityResultPresenter();
    }
}

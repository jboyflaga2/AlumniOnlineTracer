package example.com.sampleproject.core;

import javax.inject.Singleton;

import example.com.sampleproject.EmploymentView;
import example.com.sampleproject.MainView;
import example.com.sampleproject.PersonalInfoView;

/**
 * Created by User-01 on 12/25/2016.
 */

@dagger.Component(modules = MainModule.class)
@Singleton
public interface MainComponent {
    void inject(MainView t);
    void inject(PersonalInfoView view);
    void inject(EmploymentView view);
    void inject(WindowOwnerPresenter view);
    void inject(MainActivity mainActivity);
    //void inject(CallbackManager callbackManager);
}

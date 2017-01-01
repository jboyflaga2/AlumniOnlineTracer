package example.com.sampleproject;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.com.sampleproject.core.Layout;
import mortar.ViewPresenter;

/**
 * Created by User-01 on 12/24/2016.
 */

@Layout(R.layout.employment_view)
public class EmploymentScreen {

//    @dagger.Component
//    @Singleton
//    interface Component {
//        void inject(EmploymentView view);
//    }

    @Singleton
    static class Presenter extends ViewPresenter<EmploymentView> {

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {

        }

        @Override
        protected void onSave(Bundle outState) {

        }
    }
}

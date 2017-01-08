package example.com.sampleproject;

import android.content.SharedPreferences;
import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.com.sampleproject.core.Layout;
import example.com.sampleproject.models.PersonalInfo;
import example.com.sampleproject.services.PersonalInfoService;
import mortar.ViewPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        private PersonalInfo currentPersonalInfo;

        private final PersonalInfoService personalInfoService;

        @Inject
        Presenter(PersonalInfoService personalInfoService) {
            this.personalInfoService = personalInfoService;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            String userId = "";
            SharedPreferences prefs = getView().getContext().getSharedPreferences("PREFS", 0);
            userId = prefs.getString("USER_ID", "");

            Call<PersonalInfo> call = personalInfoService.getPersonalInfo(userId);
            call.enqueue(new Callback<PersonalInfo>() {
                @Override
                public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {
                    if (response.body() != null) {
                        currentPersonalInfo = response.body();
                        getView().fillInputs(currentPersonalInfo);
                    }
                }

                @Override
                public void onFailure(Call<PersonalInfo> call, Throwable t) {

                }
            });
        }

        @Override
        protected void onSave(Bundle outState) {

        }

        public void updateEmploymentInfo(PersonalInfo employmentInfo) {
            currentPersonalInfo.setEmployed(employmentInfo.isEmployed());
            Call<PersonalInfo> call = personalInfoService.postPersonalInfo(currentPersonalInfo);
            call.enqueue(new Callback<PersonalInfo>() {
                @Override
                public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {

                }

                @Override
                public void onFailure(Call<PersonalInfo> call, Throwable t) {

                }
            });
        }
    }
}

package example.com.sampleproject;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.AccessToken;

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

@Layout(R.layout.personal_info_view)
public class PersonalInfoScreen {

//    @dagger.Component
//    @Singleton
//    interface Component {
//        void inject(PersonalInfoView view);
//    }

    @Singleton
    static class Presenter extends ViewPresenter<PersonalInfoView> {

        private final PersonalInfoService personalInfoService;

        @Inject
        Presenter(PersonalInfoService personalInfoService) {
            this.personalInfoService = personalInfoService;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            String userId = "";
            //if (isLoggedIn()) {
                SharedPreferences prefs = getView().getContext().getSharedPreferences("PREFS", 0);
                userId = prefs.getString("USER_ID", "");
            //}

            Call<PersonalInfo> call = personalInfoService.getPersonalInfo(userId);
            call.enqueue(new Callback<PersonalInfo>() {
                @Override
                public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {
                    if (response.body() != null) {
                        CurrentPersonalInfo = response.body();
                        getView().fillInputs(CurrentPersonalInfo);
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

        public boolean isLoggedIn() {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            return accessToken != null;
        }

        public void updatePersonalInfo() {
            Call<PersonalInfo> call = personalInfoService.postPersonalInfo(CurrentPersonalInfo);
            call.enqueue(new Callback<PersonalInfo>() {
                @Override
                public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {

                }

                @Override
                public void onFailure(Call<PersonalInfo> call, Throwable t) {

                }
            });
        }

        public PersonalInfo CurrentPersonalInfo;
    }
}

package example.com.sampleproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

import example.com.sampleproject.core.ActionBarOwner;
import example.com.sampleproject.core.Layout;
import example.com.sampleproject.models.PersonalInfo;
import example.com.sampleproject.services.PersonalInfoService;
import mortar.ViewPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action0;

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
        private final ActionBarOwner actionBar;

        @Inject
        Presenter(PersonalInfoService personalInfoService,  ActionBarOwner actionBar) {
            this.personalInfoService = personalInfoService;
            this.actionBar = actionBar;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            SharedPreferences prefs = getView().getContext().getSharedPreferences("PREFS", 0);
            String userId = prefs.getString("USER_ID", "");

            // TODO: 1/10/2017 this is temporary
            if (userId == null || userId.isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_ID", "jboy");
                editor.commit();
            }

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


//            ActionBarOwner.Config actionBarConfig = new ActionBarOwner.Config(true, true, "A", new ActionBarOwner.MenuAction("End", new Action0() {
//                        @Override public void call() {
//                            Toast.makeText(getView().getContext(), "Clicked End", Toast.LENGTH_LONG).show();
//                        }
//                    }));
//
////            ActionBarOwner.Config actionBarConfig = actionBar.getConfig();
//            actionBarConfig.withAction(new ActionBarOwner.MenuAction("End 2", new Action0() {
//                @Override public void call() {
//                    Toast.makeText(getView().getContext(), "Clicked End 2", Toast.LENGTH_LONG).show();
//                }
//            }));
//
//            actionBar.setConfig(actionBarConfig);

        }

        @Override
        protected void onSave(Bundle outState) {

        }

        public void updatePersonalInfo(PersonalInfo personalInfoToUpdate) {
            Call<PersonalInfo> call = personalInfoService.postPersonalInfo(personalInfoToUpdate);
            call.enqueue(new Callback<PersonalInfo>() {
                @Override
                public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {
                    getView().goToEmploymentScreen();
                }

                @Override
                public void onFailure(Call<PersonalInfo> call, Throwable t) {
                    getView().goToEmploymentScreen();
                }
            });
        }

        public PersonalInfo CurrentPersonalInfo;
    }
}

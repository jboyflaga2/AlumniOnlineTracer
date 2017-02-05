package example.com.sampleproject;

import android.os.Bundle;

import java.util.List;

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
 * Created by User-01 on 1/9/2017.
 */

@Layout(R.layout.chart_view)
public class ChartScreen {


    @Singleton
    static class Presenter extends ViewPresenter<ChartView> {

        private List<PersonalInfo> personalInfoList;

        private final PersonalInfoService personalInfoService;

        @Inject
        Presenter(PersonalInfoService personalInfoService) {
            this.personalInfoService = personalInfoService;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Call<List<PersonalInfo>> call = personalInfoService.getAllPersonalInfo();
            call.enqueue(new Callback<List<PersonalInfo>>() {
                @Override
                public void onResponse(Call<List<PersonalInfo>> call, Response<List<PersonalInfo>> response) {
                    if (response.body() != null) {
                        personalInfoList = response.body();

                        int nEmployed = 0;
                        int nNotEmplyed = 0;
                        for (PersonalInfo info :
                                personalInfoList) {
                            if (info.isEmployed()) {
                                nEmployed++;
                            } else {
                                nNotEmplyed++;
                            }
                        }
                        getView().showChart(nEmployed, nNotEmplyed);
                    }
                }

                @Override
                public void onFailure(Call<List<PersonalInfo>> call, Throwable t) {

                }
            });
        }

        @Override
        protected void onSave(Bundle outState) {

        }
    }
}

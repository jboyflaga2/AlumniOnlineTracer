package example.com.sampleproject.services;

import example.com.sampleproject.models.PersonalInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by User-01 on 12/26/2016.
 */

public interface PersonalInfoService {

    @GET("/api/personalinfo/{id}")
    Call<PersonalInfo> getPersonalInfo(@Path("id") String id);

    @POST("/api/personalinfo")
    Call<PersonalInfo> postPersonalInfo(@Body PersonalInfo personalInfo);

}

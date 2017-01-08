package example.com.sampleproject.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by User-01 on 12/24/2016.
 */

public interface ValuesService {

    @GET("/api/values")
    Call<List<String>> getValues();

//    public List<String> getValues() {
//        List<String> values = new ArrayList<>();
//        values.add("value1");
//        values.add("value2");
//
//        return values;
//    }
}

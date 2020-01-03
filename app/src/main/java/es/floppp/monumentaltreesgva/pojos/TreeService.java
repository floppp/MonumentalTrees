package es.floppp.monumentaltreesgva.pojos;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface TreeService {
    @GET("publicApi/{region}")
    Call<JsonObject> getTrees(@Path("region") String region);
}

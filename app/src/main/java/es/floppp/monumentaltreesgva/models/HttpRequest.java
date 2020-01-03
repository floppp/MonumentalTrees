package es.floppp.monumentaltreesgva.models;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import es.floppp.monumentaltreesgva.extras.CustomCallback;
import es.floppp.monumentaltreesgva.extras.Deserializer;
import es.floppp.monumentaltreesgva.extras.K;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.pojos.TreeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpRequest implements Callback<JsonObject> {

    private String mEndPoint;
    private CustomCallback mCallback;

    public HttpRequest(CustomCallback callback) {
        this.mCallback = callback;
    }
    public void makeTreesRequest(K.Region endPoint) {
        this.mEndPoint = K.END_POINTS.get(endPoint);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(K.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TreeService service = retrofit.create(TreeService.class);

        Call<JsonObject> call = service.getTrees(mEndPoint);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        List<Tree> trees = new ArrayList<>();
        Deserializer deserializer = new Deserializer();
        if(response.isSuccessful()) {
            response.body()
                    .getAsJsonArray(this.mEndPoint)
                    .forEach((json) -> trees.add(deserializer.jsonToTree(json, this.mEndPoint)));
            this.mCallback.callback(trees);
        } else {
            Log.d("PRUEBAS", response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.d("PRUEBAS", t.getLocalizedMessage());
    }
}

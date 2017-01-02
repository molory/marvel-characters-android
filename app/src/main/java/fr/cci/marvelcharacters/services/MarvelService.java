package fr.cci.marvelcharacters.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MarvelService {

    @GET("v1/public/characters")
    Call<ResponseBody> characters(@Query("apikey") String apikey, @Query("ts") String ts, @Query("hash") String hash, @Query("limit") int limit);

}


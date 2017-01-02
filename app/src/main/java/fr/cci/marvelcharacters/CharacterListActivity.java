package fr.cci.marvelcharacters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.cci.marvelcharacters.services.MarvelService;
import fr.cci.marvelcharacters.utils.SecurityUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CharacterListActivity extends AppCompatActivity implements CharacterAdapterListener{

    private final static String API_BASE_URL = "https://gateway.marvel.com:443";

    private final static String PUBLIC_API_KEY = "7d6c5ccfaba7dcb62bb4e1d1e7a5407f";
    private final static String PRIVATE_API_KEY = "f757ffb6a38a443541249019742dd0c2b3ee5634";

    private RecyclerView rvMain;

    private ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        setupViews();

        final Retrofit retrofit = buildRetrofit();

        fetchCharacters(retrofit);
    }

    @Override
    public void onCharacterClick(final CharacterModel characterModel) {
        final Intent intent = CharacterDetailsActivity.newIntent(this, characterModel);
        startActivity(intent);
    }

    private void setupViews() {
        rvMain = (RecyclerView) findViewById(R.id.rv_main);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMain.setLayoutManager(linearLayoutManager);

        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle(R.string.com_loading);
    }

    private Retrofit buildRetrofit() {

        /********** This allows to enable Retrofit debug log **************/
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(logging);
        /******************************************************************/

        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClientBuilder.build())
                .build();
    }

    private void fetchCharacters(final Retrofit retrofit) {
        final MarvelService marvelService = retrofit.create(MarvelService.class);

        final String publicApiKey = PUBLIC_API_KEY;
        final String privateApiKey = PRIVATE_API_KEY;
        final String ts = String.valueOf(System.currentTimeMillis());
        final String hash = SecurityUtils.md5(ts+privateApiKey+publicApiKey);

        showLoading();
        Call<ResponseBody> call = marvelService.characters(publicApiKey, ts, hash, 100);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.code() == 200) {
                    onCharactersFetchSuccess(response.body());
                }else {
                    onCharactersFetchError();
                }
                hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                onRetrofitError();
            }
        });
    }

    private void onCharactersFetchSuccess(final ResponseBody response) {
        try {
            final String jsonStr = response.string();
            final JSONObject jsonObject = new JSONObject(jsonStr);
            final JSONObject jsonData = jsonObject.getJSONObject("data");
            final JSONArray jsonResults = jsonData.getJSONArray("results");

            List<CharacterModel> characterModelList = new ArrayList<>();
            for(int i = 0 ; i < jsonResults.length(); i++) {
                final JSONObject jsonCharacter = jsonResults.getJSONObject(i);

                final String name = jsonCharacter.getString("name");
                final String description = jsonCharacter.getString("description");

                final JSONObject jsonThumbnail= jsonCharacter.getJSONObject("thumbnail");
                final String path = jsonThumbnail.getString("path");
                final String extension = jsonThumbnail.getString("extension");

                CharacterModel characterModel = new CharacterModel(name, description, path, extension);
                characterModelList.add(characterModel);
            }

            CharacterAdapter adapter = new CharacterAdapter(this, characterModelList);
            rvMain.setAdapter(adapter);
        } catch (IOException | JSONException exception) {
            onParsingError();
        }
    }

    private void onCharactersFetchError() {
        showToast(R.string.error_fetch_characters);
    }

    private void onRetrofitError() {
        showToast(R.string.error_network);
    }

    private void onParsingError() {
        showToast(R.string.error_network);
    }

    private void showToast(@StringRes final int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
    }

    private void showLoading() {
        pdLoading.show();
    }

    private void hideLoading() {
        pdLoading.hide();
    }

}

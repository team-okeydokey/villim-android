package net.villim.villim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialize mapview & Google Play Services */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();

        /* Update user info */
        VillimSession session = new VillimSession(getApplicationContext());
        if (session.getLoggedIn()) {

            final OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!response.isSuccessful()) try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Request success. */
            try {
                /* 주의: response.body().string()은 한 번 부를 수 있음 */
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {
                    VillimUser user = VillimUser.createUserFromJSONObject((JSONObject) jsonObject.get(KEY_USER_INFO));
                    session.updateUserSession(user);
                } else {

                }
            } catch (JSONException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /* Launch MainActivity */
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

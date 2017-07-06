package commerce.ssuk;

/**
 * Created by prince on 7/6/17.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    String imag;private ImageView aimg;private TextView name;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    aimg=(ImageView)findViewById(R.id.imgLogo);
        name=(TextView)findViewById(R.id.name);
        String imageURL="https://www.dg.uk/wp-content/themes/dg%20placeholder/img/logo.png";
          delvat(this);



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this,HomActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }




    public  void delvat(final Context cm)
    {  String urldel ="http://192.168.43.227:8000/api/logo/";


        JsonObjectRequest req = new JsonObjectRequest(urldel+"58e32243eaf87011c22bc744",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reps", response.toString());

                        try {



                            imag = response.getString("pic");
                            Log.e("image gor",imag);
                            name.setText(response.getString("name"));



                            Picasso.with(cm)
                                    .load("http://192.168.43.227:8000"+imag).resize(100,100)
                                    .into(new Target()
                                    {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                                        {
                                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                                            aimg.setImageDrawable(d);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable)
                                        {
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable)
                                        {
                                        }
                                    });







                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(cm,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Resp", "Error: " + error.getMessage());
                Toast.makeText(cm,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(req);



    }



















}
package commerce.ssuk;

/**
 * Created by prince on 7/6/17.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView img=(ImageView)findViewById(R.id.imgLogo);
        String imageURL="https://www.dg.uk/wp-content/themes/dg%20placeholder/img/logo.png";

        final ActionBar ab = getSupportActionBar();
        Picasso.with(this)
                .load(imageURL).resize(100,100)
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                        img.setImageDrawable(d);
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

}
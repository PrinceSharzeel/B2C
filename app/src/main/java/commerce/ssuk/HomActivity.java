package commerce.ssuk;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomActivity extends AppCompatActivity {
    static Button notifCount;
    static int mNotifCount = 0;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public Context mcontext;
    private ViewPager viewPager; private int[] tabIcons = {
            R.drawable.ic_account_balance_black_24dp,

            R.drawable.ic_add_shopping_cart_black_24dp,
            R.drawable.order,
            R.drawable.ic_local_grocery_store_black_24dp,
            R.drawable.ic_account_balance_wallet_black_24dp,
    };



    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);
        ctx = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mcontext=this;
      //  toolbar.setLogo(R.drawable.icon);
       setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LogoSet(this);// sets shop icon on toolbar



        NotifTap();// action handling on tapping notifications


        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }



        Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
        startService(intent);



        final DBAdapter db=new DBAdapter(this);
        db.open();
        db.close();


        Session();//Maintains and checks Login sessions





        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }





    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }





    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Home");
        adapter.addFragment(new FavFragment(), "Shop");

        adapter.addFragment(new PrevOrder(), "Orders");
        adapter.addFragment(new OrderFrag(), "Trolley");
        adapter.addFragment(new AccountFrag() , "Account");


        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }



        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        MenuItem menuItem = menu.findItem(R.id.testAction);
        final DBAdapter db=new DBAdapter(getApplicationContext());
        db.open();
        SharedPreferences pref=getApplication().getSharedPreferences("session",0);
        Log.e("NUmber",db.NumberOfItems(pref.getString("contact",null))+"");
        menuItem.setIcon(buildCounterDrawable(db.NumberOfItems(pref.getString("contact",null)),  R.drawable.searchtrolley));
        db.close();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.testAction)
        {
            tabLayout.setSmoothScrollingEnabled(true);
            tabLayout.setScrollPosition(3, 0f, true);
            TabLayout.Tab tab = tabLayout.getTabAt(3);
            tab.select();

        }



        return super.onOptionsItemSelected(item);
    }





/// To show notification count on toolbar
    public  Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }



    //Session maintaining fucntion




    public void Session()
    {

        SharedPreferences pref=getApplication().getSharedPreferences("session",0);
        Log.e("LOLOLOLOX",pref.getString("status",null)+"");
        if(!pref.contains("status"))
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("status","loggedout");
            editor.apply();
            Log.e("LOLOLOLO",pref.getString("status",null)+"");

        }



    }

    
    
    
    //Unused and not working
    
    public void NotifTap()
    {

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("Dsds", "Key: " + key + " Value: " + value);
            }
        }
        
        
        
    }
    
    
    
    
    
    
    
    
    




    public  void LogoSet(final Context cm)
    {  String urldel ="http://192.168.43.227:8000/api/logo/";


        JsonObjectRequest req = new JsonObjectRequest(urldel+"58e32243eaf87011c22bc744",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {



                           String imag = response.getString("pic");





                            final ActionBar ab = getSupportActionBar();
                            Picasso.with(cm)
                                    .load("http://192.168.43.227:8000"+imag).resize(100,100)
                                    .into(new Target()
                                    {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                                        {
                                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                                            ab.setIcon(d);
                                            ab.setDisplayShowHomeEnabled(true);
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
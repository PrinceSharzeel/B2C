package commerce.ssuk;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

            R.drawable.ic_thumb_up_black_24dp,
            R.drawable.ic_local_grocery_store_black_24dp,
            R.drawable.ic_account_balance_wallet_black_24dp,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mcontext=this;
      //  toolbar.setLogo(R.drawable.icon);
       setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    String imageURL="https://www.dg.uk/wp-content/themes/dg%20placeholder/img/logo.png";

        final ActionBar ab = getSupportActionBar();
        Picasso.with(this)
                .load(imageURL).resize(62,62)
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


        final DBAdapter db=new DBAdapter(this);
        db.open();
        db.close();





        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Home");
        adapter.addFragment(new FavFragment(), "Shop");
        adapter.addFragment(new ProductDetailFrag(), "Favorites");
        adapter.addFragment(new OrderFrag(), "Order");
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
        Log.e("NUmber",db.NumberOfItems()+"");
        menuItem.setIcon(buildCounterDrawable(db.NumberOfItems(),  R.drawable.searchtrolley));
        db.close();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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




    }
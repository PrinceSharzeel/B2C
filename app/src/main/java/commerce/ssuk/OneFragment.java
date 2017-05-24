package commerce.ssuk;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment{
    public static String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";

    // json array response url
    private static String urlJsonArry = "http://api.androidhive.info/volley/person_array.json";

    private static String TAG = MainActivity.class.getSimpleName();


    // Progress dialog
    private static ProgressDialog pDialog;

    private static TextView txtResponse;

    // temporary string to show the parsed response
    private static String jsonResponse;
    private static Context context;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Item> albumList;
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.car1, R.drawable.car2,R.drawable.car3};

    private ImageView trans;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_main, container, false);
        context=getContext();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);



        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), albumList);
        trans=(ImageView)view.findViewById(R.id.trans);

        NestedScrollView scrollView = (NestedScrollView)view.findViewById(R.id.nsv);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        carouselView = (CarouselView)view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);



        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView.setImageListener(imageListener);


       // prepareAlbums();

        try {
            //Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
            Picasso.with(getContext())
                    .load("http://cdn2.stylecraze.com/wp-content/uploads/2014/03/3697-Slogans-On-Healthy-Food.jpg")
                    .resize(50, 50)
                    .centerCrop()
                    .into(trans);
        } catch (Exception e) {
            e.printStackTrace();
        }

prepareAlbums();


        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person
                                        .getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                Item a=new Item(name,email,R.drawable.bg);
                                albumList.add(a);
                                Log.e("resultsuij",name);

                                adapter.notifyDataSetChanged();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);





        return view;
    }











    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.bg,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,};

        Item a = new Item("Organic", "2", covers[0]);
        albumList.add(a);

        a = new Item("Stored Food","2", covers[1]);
        albumList.add(a);

        a = new Item("Dairy Products", "2", covers[2]);
        albumList.add(a);

        a = new Item("Drinks", "2", covers[3]);
        albumList.add(a);


        adapter.notifyDataSetChanged();
    }




    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }














}
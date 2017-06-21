package commerce.ssuk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static commerce.ssuk.AppController.TAG;
import static commerce.ssuk.AppController.getInstance;

/**
 * Created by princes on 24-May-17.
 */
public class ProductListFrag  extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Orders> albumList;
    Context context;
    SwipeRefreshLayout swipeLayout;


    DBAdapter db;
    private static String urlJsonArry = "http://192.168.43.227:8000/api/products/";


    public void ProductListFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.productlistfrag,container,false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().setIcon();
        //Recycler
        getActivity().invalidateOptionsMenu();
        final Masker obj=new Masker();
        final ProgressBar pb=(ProgressBar)view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        context=getContext();
        String value = getArguments().getString("category");
        albumList=new ArrayList<>();
// Listener to ADD button in product list





        adapter = new ProductAdapter(context,albumList, new ProductAdapter.MyAdapterListener() {
            @Override
            public void AddCartViewOnClick(final View v, final int position, final String a) {
                String TAG = "Dwdw";
                Log.d(TAG, "iconTextViewOnClick at position " + position);
                Log.e("Tag", albumList.get(position).getName() + "");

                if (!obj.LoginCheck(getActivity())) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.InvitationDialog);

                    builder.setTitle("Login required");builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            movetologin();
                            return;

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }

              else{
                db = new DBAdapter(getContext());
                try {


                    db.open();
                    final SharedPreferences pref =getContext().getSharedPreferences("session",0);

                    final String fe = db.checkItem(albumList.get(position).getName() + "", pref.getString("contact",null));
                    Log.e("old", fe);

                    if (fe.equals("true")) {
                        Time now = new Time();
                        now.setToNow();
                        String sTime = now.format("%Y_%m_%d %T");
                        db.insertCart(albumList.get(position).getName() + "", albumList.get(position).getPrice() + "", a, sTime, pref.getString("contact",null));
                            Log.e("contacccccinsertct",pref.getString("contact",null));
                        Toast.makeText(getActivity(), "Added to Cart",
                                Toast.LENGTH_SHORT).show();
                        db.close();

                    } else {


                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), R.style.InvitationDialog);
                        alert.setTitle("Already added");
                        alert.setMessage("Added " + fe + " items already\nDo you want to add " + a + " more?");

                        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                                Log.e("old", fe);

                                int quant = Integer.parseInt(fe);
                                Log.e("quant", quant + "");
                                int new_quant = Integer.parseInt(a);
                                Log.e("nquant", new_quant + "");
                                quant = quant + new_quant;
                                Log.e("Totalquant", quant + "");
                                db.open();
                                Time now = new Time();
                                now.setToNow();
                                String sTime = now.format("%Y_%m_%d %T");
                                db.updateCart(albumList.get(position).getName() + "", quant + "", sTime, pref.getString("contact",null));
                                Toast.makeText(getActivity(), "Added to Cart",
                                        Toast.LENGTH_SHORT).show();


                            }
                        });
                        alert.show();

                        db.close();

                    }

                    getActivity().invalidateOptionsMenu();


                } catch (Exception e) {
                    Log.e("Db", "Ds");
                }


            }



            }

        });












        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutmanager=new GridLayoutManager(getActivity(),2);




        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);









//Parsing the Rest API
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry+value,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pb.setVisibility(View.GONE);
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("ptitle");
                                String price = person.getString("price");

                                String weight = person.getString("pweight");

                                String litre = person.getString("plitres");

                                String prof = person.getString("ppro");

                                long a= Integer.parseInt(person.getString("preddisc"));
                                a=Integer.parseInt(person.getString("price"))+a;
                                String redprice=a+"";


                                Orders prod=new Orders(name,price,prof,weight+" gm| "+litre,redprice,person.getString("punit"));
                                albumList.add(prod);
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
            public void onErrorResponse(VolleyError volleyError) {


                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }



                Toast.makeText(context,
                        message, Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);





















        return view;



    }


    public String order_valid(String entered,String available,String previous)

    {
        int enter,avail,prev;
        prev=Integer.parseInt(previous);
        enter=Integer.parseInt(entered);
        avail=Integer.parseInt(available);

      int ordered= prev+enter;
        if(ordered<=avail)
        {
            return "true";
        }
        avail=avail-prev;
        if(avail==0) return "No more";
        return avail+" more";




    }



    public void movetologin()
    {

        ViewPager viewPager = (ViewPager) getActivity().findViewById(
                R.id.viewpager);
        viewPager.setCurrentItem(3);
    }




}

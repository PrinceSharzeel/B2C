package commerce.ssuk;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import java.util.ArrayList;
import java.util.List;

import static commerce.ssuk.AppController.TAG;


public class PrevOrder extends Fragment{
    private static String urlJsonArry = "http://192.168.43.227:8000/api/orderlist";
    private static ProgressDialog pDialog;private RecyclerView recyclerView;
    private List<PrevObs> albumList;
    Context context; private ImageView trans;PrevOrdAdap1 adapter;

    public PrevOrder() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.prevord, container, false);
        context=getContext();







        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        CardView logcard=(CardView)view.findViewById(R.id.card_login);
        logcard.setVisibility(View.GONE);

        albumList = new ArrayList<>();
        adapter = new PrevOrdAdap1(getContext(), albumList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode

///Prevent to open orders list if not logged in
     if(!pref.contains("status"))
     {
           //movetologin();
         logcard.setVisibility(View.VISIBLE);

     }
  else if(!pref.getString("status",null).equals("logged")){

         //movetologin();
         logcard.setVisibility(View.VISIBLE);

     }
     else {
         logcard.setVisibility(View.GONE);

//Parsing the Rest API

         JsonArrayRequest req = new JsonArrayRequest(urlJsonArry + "/" + pref.getString("contact", null),
                 new Response.Listener<JSONArray>() {
                     @Override
                     public void onResponse(JSONArray response) {

                         Log.d(TAG, response.toString());

                         try {
                             for (int i = 0; i < response.length(); i++) {

                                 JSONObject person = (JSONObject) response
                                         .get(i);

                                 String name = person.getString("contact");
                                 String price = person.getString("ord_date");

                                 String weight = person.getString("status");



                                 PrevObs prod = new PrevObs(person.getString("quant")+" Items","Ordered On "+ price,"Total : £ "+ person.getString("price"), person.getString("quant"),weight, 1,name);
                                 albumList.add(prod);
                                 Log.e("resultsuij", name);

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


     }
        return view;
    }











    public void movetologin()
    {  Log.e("move","%%%%%%%%%%%%%%%%%%%%%%%%0");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.InvitationDialog);

        builder.setTitle("Login required");builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {

            ViewPager viewPager = (ViewPager) getActivity().findViewById(
                    R.id.viewpager);
            viewPager.setCurrentItem(4);

        }
    });
        AlertDialog alert = builder.create();
        alert.show();

    }
















}
package commerce.ssuk;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static commerce.ssuk.AppController.TAG;


public class PrevOrder2 extends Fragment {
    private static String urlJsonArry2 = "http://192.168.43.227:8000/api/orderlist2/";

    private static String urlJsonArry = "http://192.168.43.227:8000/api/orderlist";

    private static String urlinvoice = "http://192.168.43.227:8000/api/invoice";
    private static ProgressDialog pDialog;
    private RecyclerView recyclerView;private ImageView sign;
    private List<PrevObs2> albumList;
    Context context;TextView date,stat,quant,price,delcharge,invoice;
    private ImageView trans;
    PrevOrdAdap2 adapter;

    public PrevOrder2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.prevord2, container, false);
        context = getContext();
     price=(TextView)view.findViewById(R.id.price);
       date=(TextView)view.findViewById(R.id.date);
        stat=(TextView)view.findViewById(R.id.count);
        quant=(TextView)view.findViewById(R.id.quant);
        sign=(ImageView)view.findViewById(R.id.signal);

        invoice=(TextView)view.findViewById(R.id.vat);

        delcharge=(TextView)view.findViewById(R.id.delcharge);
        delcharge.setCompoundDrawablesWithIntrinsicBounds( 0, 0,R.drawable.ic_add_shopping_cart_black_24dp,0);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new PrevOrdAdap2(getContext(), albumList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        final String id = getArguments().getString("ord_id");



//Parsing the Rest API





   order_details();

//Parsing the Rest API
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry2 + id,
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

                                String quant = person.getString("status");

                                String prof = person.getString("quant");
                                String del=person.getString("price");
                                String[] parts = del.split("#");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1];
                                if(!part1.equals("null"))
                                  part1="Vat : "+part1+" %";
                                else
                                    part1="";
                                if(!part2.equals("null")) {
                                    part2 = " | Delivery £ " + part2+" |";

                                    delcharge.setText(part1 + " " + part2);
                                    delcharge.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_shopping_cart_black_24dp, 0);
                                }
                                else {
                                    part2 = "";
                                    delcharge.setText(part1 + " " + part2);
                                    delcharge.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                }



                                PrevObs2 prod = new PrevObs2(name, "Price per unit : £ "+price, prof, quant);
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






        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail_pdf(id);



            }
        });
















        return view;


    }






   // 0 - for private mode



    public void order_details()
    {
        SharedPreferences pref = getContext().getSharedPreferences("session", 0);
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry + "/" + pref.getString("contact", null),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);


                               date.setText("Dated :"+person.getString("ord_date"));
                                stat.setText(person.getString("status"));
                                quant.setText(person.getString("quant")+" Items");
                                price.setText("Total Price: £ "+person.getString("price"));
                                if(person.getString("status").equals("slotted"))
                                    Picasso.with(getContext()).load(R.drawable.progress).into(sign);

                                else
                                    Picasso.with(getContext()).load(R.drawable.delivered).into(sign);




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






    public void mail_pdf(final String id)
    {  final ProgressDialog dialog = ProgressDialog.show(getContext(), null, null);
        ProgressBar spinner = new android.widget.ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();


        JsonObjectRequest req = new JsonObjectRequest(urlinvoice + "/58e32243eaf87011c22bc744/"+id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       dialog.dismiss();
                        Log.d("cbkdsbckjsbdc", urlinvoice + "/" +id+"/58e32243eaf87011c22bc744");
                        Log.d(TAG, response.toString());


                        try {

                                    if(response.getString("ok").equals("Invoice has been Mailed"))
                                    {
                                        invoice.setText("Invoice Mailed to your email");
                                    }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();

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










}













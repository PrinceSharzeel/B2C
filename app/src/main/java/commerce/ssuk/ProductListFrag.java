package commerce.ssuk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        context=getContext();
        String value = getArguments().getString("category");
        albumList=new ArrayList<>();
// Listener to ADD button in product list
        adapter = new ProductAdapter(context,albumList, new ProductAdapter.MyAdapterListener() {
            @Override
            public void AddCartViewOnClick(final View v, final int position, final String a) {
                String TAG="Dwdw";
                Log.d(TAG, "iconTextViewOnClick at position "+position);
                Log.e("Tag",albumList.get(position).getName()+"");

             db = new DBAdapter(getContext());
                try {


                    db.open();
                    final String fe = db.checkItem(albumList.get(position).getName() + "");
                    Log.e("old",fe);

                    if (fe.equals("true")) {

                        if(order_valid(a,albumList.get(position).getUnits(),"0").equals("true")) {

                            db.insertCart(albumList.get(position).getName() + "", albumList.get(position).getPrice() + "", a);

                            Toast.makeText(getActivity(), "Inserted",
                                    Toast.LENGTH_SHORT).show();
                            db.close();
                        }
                        else{
                            Toast.makeText(getActivity(),"Limit of order is: "+order_valid(a,albumList.get(position).getUnits(),"0"),
                                    Toast.LENGTH_SHORT).show();}

                    }
                    else{


                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(),AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                        alert.setTitle("Already added");
                        alert.setMessage("Added "+fe+" items already\nDo you want to add "+a+" more?");

                        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                       if((order_valid(a,albumList.get(position).getUnits(),fe).equals("true")))

                                {        db.open();

                                Log.e("old",fe);

                                int quant=Integer.parseInt(fe);
                                Log.e("quant",quant+"");
                                int new_quant=Integer.parseInt(a);
                                Log.e("nquant",new_quant+"");
                                quant=quant+new_quant;
                                Log.e("Totalquant",quant+"");
                                db.updateCart(albumList.get(position).getName() + "",quant+"");
                                    Toast.makeText(getActivity(), "Inserted",
                                            Toast.LENGTH_SHORT).show();}


                         else
                                {
                                    Toast.makeText(getActivity(),"You can add "+order_valid(a,albumList.get(position).getUnits(),fe),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        alert.show();

                        db.close();

                    }

                        getActivity().invalidateOptionsMenu();


                    }
                catch(Exception e){
                        Log.e("Db", "Ds");
                    }










            }

        });












        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
       //int spancount= (int) Math.ceil( data.size() / 3f )
        RecyclerView.LayoutManager mLayoutmanager=new GridLayoutManager(getActivity(),2);




        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);






//Parsing the Rest API
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry+value,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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





}

package commerce.ssuk;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by princes on 24-May-17.
 */
public class ProductDetailFrag  extends Fragment{
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Item> albumList;
    String name,email;TextView title,extra,brand,price,max,discount,discper,discexp,min,weight,disc,unit;ImageView thumbnail;
    Button add; private static String urlJsonArry = "http://192.168.43.227:8000/api/prod_detail/";

    public void ProductDetailFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.prod_detail,container,false);



//       Log.e("ggg",getArguments().toString());

        title=(TextView)view.findViewById(R.id.count0);
        price=(TextView)view.findViewById(R.id.count);
        weight=(TextView)view.findViewById(R.id.title);
        unit=(TextView)view.findViewById(R.id.desc);
        thumbnail=(ImageView)view.findViewById(R.id.thumbnail);
        add=(Button)view.findViewById(R.id.add);
        discount=(TextView)view.findViewById(R.id.discount);
        discper=(TextView)view.findViewById(R.id.discper);
        discexp=(TextView)view.findViewById(R.id.discexp);

        brand=(TextView)view.findViewById(R.id.brand);
        disc=(TextView)view.findViewById(R.id.disc);









                JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Reps", response.toString());

                        try {

                            final JSONObject person = (JSONObject) response
                                    .get(0);

                            name = person.getString("ptitle");
                            email = person.getString("price")+" €";
                            title.setText(name);price.setText(email);
                            weight.setText(person.getString("pweight")+" kg");
                            unit.setText(person.getString("punit")+" p/gm");
                            Log.e("imageew","http://192.168.43.227:8000");
                            Picasso.with(getContext()).load("http:/\n" +
                                    "                            disc.setText(person.getString(\"pdisc\"));/192.168.43.227:8000"+person.getString("ppro")
                            ).into(thumbnail);
                            brand.setText(person.getString("pbrand"));
                            discper.setText("Discount "+person.getString("pdisc")+" %");
                            discexp.setText("Till: "+person.getString("pexp"));



                            long a= Integer.parseInt(person.getString("preddisc"));
                            a=Integer.parseInt(person.getString("price"))+a;
                            discount.setText(a+" €");
                            discount.setPaintFlags(discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {



                                    final DBAdapter db = new DBAdapter(getContext());
                                    try {
                                        db.open();
                                        db.insertCart(person.getString("ptitle"),person.getString("price"),"a");

                                        Toast.makeText(getActivity(), "Added to trolley",
                                                Toast.LENGTH_SHORT).show();

                                        db.close();
                                        getActivity().invalidateOptionsMenu();
                                    }
                                    catch (Exception e){Log.e("Db","Ds");}



                                }
                            });






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Resp", "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);












        return view;



    }




}

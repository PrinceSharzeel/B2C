package commerce.ssuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ProductDetail extends AppCompatActivity {

    private static String urlJsonArry = "http://192.168.43.227:8000/api/prod_detail/";
        String name,email;TextView title,extra,brand,price,max,discount,discper,discexp,min,weight,disc,unit;ImageView thumbnail;
Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_detail);


        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        String Price = intent.getExtras().getString("Price");

        title=(TextView)findViewById(R.id.count0);
        price=(TextView)findViewById(R.id.count);
        weight=(TextView)findViewById(R.id.title);
        unit=(TextView)findViewById(R.id.desc);
        thumbnail=(ImageView)findViewById(R.id.thumbnail);
  add=(Button)findViewById(R.id.add);
        max=(TextView)findViewById(R.id.max);
       discount=(TextView)findViewById(R.id.discount);
        discper=(TextView)findViewById(R.id.discper);
        discexp=(TextView)findViewById(R.id.discexp);

        min=(TextView)findViewById(R.id.min);
        extra=(TextView)findViewById(R.id.extra);
brand=(TextView)findViewById(R.id.brand);
        disc=(TextView)findViewById(R.id.disc);








        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry+Name,
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
                                disc.setText(person.getString("pdisc"));
                                Log.e("imageew","http://192.168.43.227:8000");
                                Picasso.with(getApplicationContext()).load("http://192.168.43.227:8000"+person.getString("ppro")
                                ).into(thumbnail);
                                max.setText("Max : "+person.getString("pmax"));
                                min.setText("Min : "+person.getString("pmin"));
                                brand.setText(person.getString("pbrand"));
                            discper.setText("Discount "+person.getString("pdisc")+" %");
                            discexp.setText("Till: "+person.getString("pexp"));


                                extra.setText("Package :"+person.getString("package")+"    Units Available: "+person.getString("punit")+"\n");

                              long a= Integer.parseInt(person.getString("preddisc"));
                               a=Integer.parseInt(person.getString("price"))+a;
                              discount.setText(a+" €");
                            discount.setPaintFlags(discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {



                                    final DBAdapter db = new DBAdapter(getApplicationContext());
                                    try {
                                        db.open();
                                        db.insertCart(person.getString("ptitle"),person.getString("price"));

                                        Toast.makeText(ProductDetail.this, "Added to trolley",
                                                Toast.LENGTH_SHORT).show();

                                        db.close();
                                    }
                                    catch (Exception e){Log.e("Db","Ds");}



                                }
                            });






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Resp", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);















    }



}

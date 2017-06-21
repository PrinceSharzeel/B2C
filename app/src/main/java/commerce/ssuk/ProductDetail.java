package commerce.ssuk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.color.white;
import static java.security.AccessController.getContext;

public class ProductDetail extends AppCompatActivity {

    private static String urlJsonArry = "http://192.168.43.227:8000/api/prod_detail/";
    DBAdapter db;int available;
        String name,email;TextView title,extra,vat,brand,price,max,discount,discper,discexp,min,weight,disc,unit;ImageView thumbnail;
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
       discount=(TextView)findViewById(R.id.discount);
        discper=(TextView)findViewById(R.id.discper);
        discexp=(TextView)findViewById(R.id.discexp);
        vat=(TextView)findViewById(R.id.vat);

brand=(TextView)findViewById(R.id.brand);
        disc=(TextView)findViewById(R.id.disc);






        final ProgressDialog dialog = ProgressDialog.show(ProductDetail.this, null, null);
        ProgressBar spinner = new android.widget.ProgressBar(ProductDetail.this, null, android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();


        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry+Name,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Reps", response.toString());dialog.dismiss();

                        try {

                                final JSONObject person = (JSONObject) response
                                        .get(0);

                                name = person.getString("ptitle");
                                 email = person.getString("price")+" €";
                                title.setText(name);price.setText(email);
                                weight.setText(person.getString("pweight")+" kg");
                                unit.setText(person.getString("punit")+" p/gm");
                                disc.setText(person.getString("pdisc"));
                            vat.setText("VAT: "+person.getString("pmax")+" %");
                                Log.e("imageew","http://192.168.43.227:8000");
                                Picasso.with(getApplicationContext()).load("http://192.168.43.227:8000"+person.getString("ppro")
                                ).into(thumbnail);

                                brand.setText(person.getString("pbrand"));
                            discper.setText("Discount "+person.getString("pdisc")+" %");
                            discexp.setText("Till: "+person.getString("pexp"));



                              long a= Integer.parseInt(person.getString("preddisc"));
                               a=Integer.parseInt(person.getString("price"))+a;
                              discount.setText(a+" €");
                            discount.setPaintFlags(discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                  final Masker obj=new Masker();



                            final SharedPreferences pref=getApplication().getSharedPreferences("session",0);
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {





                                    if(obj.LoginCheck(getApplication())) {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                        alert.setTitle("Enter the number of items ");
                                        final EditText input = new EditText(v.getContext());
                                        input.setTextColor(Color.WHITE);

                                        input.setGravity(Gravity.CENTER_HORIZONTAL);
                                        input.setWidth(20);
                                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        input.setRawInputType(Configuration.KEYBOARD_12KEY);
                                        alert.setView(input);
                                        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });

                                        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                db = new DBAdapter(getApplicationContext());
                                                try {
                                                    db.open();
                                                    final String fe = db.checkItem(person.getString("ptitle") + "", pref.getString("contact",null));
                                                    Log.e("old", fe);

                                                    if (fe.equals("true")) {
                                                        available = Integer.parseInt(person.getString("punit"));
                                                        int or = Integer.parseInt(input.getText().toString().trim());
                                                        Time now = new Time();
                                                        now.setToNow();
                                                        String sTime = now.format("%Y_%m_%d %T");
                                                        db.insertCart(person.getString("ptitle"), person.getString("price"), input.getText().toString().trim(), sTime, pref.getString("contact",null));
                                                        Toast.makeText(getApplicationContext(), "Added to Cart",
                                                                Toast.LENGTH_SHORT).show();
                                                        db.close();

                                                    } else {


                                                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                                        alert.setTitle("Already added");
                                                        alert.setMessage("Added " + fe + " items already\nDo you want to add " + input.getText().toString().trim() + " more?");

                                                        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                            }
                                                        });
                                                        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                                db.open();

                                                                Log.e("old", fe);

                                                                int quant = Integer.parseInt(fe);
                                                                Log.e("quant", quant + "");
                                                                int new_quant = Integer.parseInt(input.getText().toString().trim());
                                                                Log.e("nquant", new_quant + "");
                                                                quant = quant + new_quant;
                                                                Log.e("Totalquant", quant + "");
                                                                try {
                                                                    Time now = new Time();
                                                                    now.setToNow();
                                                                    String sTime = now.format("%Y_%m_%d %T");
                                                                    db.updateCart(person.getString("ptitle"), quant + "", sTime, pref.getString("contact",null));
                                                                    Toast.makeText(getApplicationContext(), "Added to Cart",
                                                                            Toast.LENGTH_SHORT).show();

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }


                                                            }
                                                        });
                                                        alert.show();

                                                        db.close();

                                                    }


                                                } catch (Exception e) {
                                                    Log.e("Db", "Ds");
                                                }


                                            }
                                        });
                                        alert.show();

                                    }


                                    else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetail.this, R.style.InvitationDialog);

                                        builder.setTitle("Login required");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {
                                                return;

                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();






                                    }


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
                VolleyLog.d("Resp", "Error: " + error.getMessage());dialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);















    }



}

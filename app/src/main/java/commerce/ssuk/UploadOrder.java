package commerce.ssuk;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class UploadOrder extends AppCompatActivity {

    private static final String FINAL_REGISTER_URL = "http://192.168.43.227:8000/api/orders/";
    public static final String KEY_NAME = "ptitle";

    public static final String KEY_CONTACT = "contact";
    public static final String KEY_QUANT = "quantity";

    public static final String KEY_PRICE = "priceunit";

    public static final String KEY_VAT= "vat";
    public static final String KEY_DEL= "delivery";
    public static final String KEY_TOTAL="total";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_order);




        try{
            final DBAdapter db = new DBAdapter(this);
            db.open();
            SharedPreferences pref =getApplication().getSharedPreferences("session",0);

            SharedPreferences pref2 =getApplication().getSharedPreferences("DelVat",0);

            Cursor c = db.getAllCart(pref.getString("contact",null));
            Log.e("contageettt",pref.getString("contact",null));


            dialog = ProgressDialog.show(this, null, null);
            ProgressBar spinner = new android.widget.ProgressBar(this, null, android.R.attr.progressBarStyle);
            spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(spinner);
            dialog.setCancelable(false);
            dialog.show();



            int i=0;
            if (c.moveToFirst())
            {
                do {

                    Float cost=Float.parseFloat(c.getString(2))*Float.parseFloat(c.getString(3));
                    Log.e("time",c.getString(5));

                    Order(c.getString(1),pref.getString("contact",null),c.getString(2),c.getString(3),pref2.getString("price",null),pref2.getString("vat",null),pref2.getString("del",null));




                } while (c.moveToNext());
            }
            dialog.dismiss();

            db.close();
            Log.e("itne h total",i+"");}
        catch (Exception e){   dialog.dismiss();
            Log.e("Db","Ds");}














    }







    public void Order(final String nam, final String cont, final String priced, final String quant, final String total, final String vat, final String del)
    {




        StringRequest stringRequest = new StringRequest(Request.Method.POST, FINAL_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response); Toast.makeText(UploadOrder.this,obj.getString("ok"),Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); dialog.dismiss();
                        Toast.makeText(UploadOrder.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_TOTAL,total);
                params.put(KEY_QUANT,quant);
                params.put(KEY_CONTACT,cont);
                params.put(KEY_NAME,nam);
                params.put(KEY_PRICE,priced);
                params.put(KEY_VAT,vat);
                params.put(KEY_DEL,del);
                Log.e("params",params.toString());
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(UploadOrder.this);
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);



    }


















}

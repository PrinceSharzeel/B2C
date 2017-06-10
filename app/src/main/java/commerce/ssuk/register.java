package commerce.ssuk;

import android.content.SharedPreferences;import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by princes on 24-May-17.
 */
public class register  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private Button sign,reg,regfinal;
    private LinearLayout invoice;
    private TextView rep;
    private EditText repswd,pswd,contact,nam,address;
    private CardView card,card1;

    private static final String REGISTER_URL = "http://192.168.43.227:8000/api/register/";

    private static final String FINAL_REGISTER_URL = "http://192.168.43.227:8000/api/reg/";

    private static String urlJsonArry = "http://192.168.43.227:8000/api/login/";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_CONTACT = "contact";

    public static final String KEY_ORDER = "orders";

    public static final String KEY_NAME = "name";
    private String repassword,password,name,adder,cont,postcode,orders;

    public static final String KEY_ADDRESS = "address";
    public static final String KEY_POSTCODE="postcode";
    public void AccountFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_register,container,false);
        repswd=(EditText)v.findViewById(R.id.repswd);
        pswd=(EditText)v.findViewById(R.id.pswd);
        contact=(EditText)v.findViewById(R.id.cont);
card=(CardView)v.findViewById(R.id.card_username);
reg=(Button)v.findViewById(R.id.register);
postcode=getArguments().getString("postcode");

        //second k=level
        card1=(CardView)v.findViewById(R.id.card_view1);address=(EditText)v.findViewById(R.id.address);
nam=(EditText)v.findViewById(R.id.name);
 regfinal=(Button)v.findViewById(R.id.regfinal);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pswd.getText().toString().trim().equals(repswd.getText().toString().trim()))
                {RegisterUser();}
                else Toast.makeText(getContext(),"Password mismatch",Toast.LENGTH_LONG).show();
            }
        });


card1.setVisibility(View.GONE);


        regfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalReg();
            }
        });







        return v;

    }








    private void RegisterUser(){
       repassword = repswd.getText().toString().trim();
        password = pswd.getText().toString().trim();

        //if(repassword!=password) return;

        name="cdcscscc";

        adder= "wdscscw";
        cont=contact.getText().toString().trim();
        orders="1";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response); Toast.makeText(getContext(),obj.getString("ok"),Toast.LENGTH_LONG).show();
                            if(obj.getString("ok").equals("true"))
                            {
                                //Hidekaro aur naya lao
                                card.setVisibility(View.GONE);
                                card1.setVisibility(View.VISIBLE);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_PASSWORD,password);
                params.put(KEY_ADDRESS,adder);
                params.put(KEY_CONTACT,cont);
                params.put(KEY_NAME,name);
                params.put(KEY_ORDER,orders);
                params.put(KEY_POSTCODE,postcode);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);
    }










    private void finalReg(){

        //if(repassword!=password) return;

        name=nam.getText().toString().trim();
        adder= address.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FINAL_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response); Toast.makeText(getContext(),obj.getString("ok"),Toast.LENGTH_LONG).show();
                            if(obj.getString("ok").equals("registered"))
                            {
                                card1.setVisibility(View.GONE);
                                SessionUpdate(cont,name,adder);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_PASSWORD,password);
                params.put(KEY_ADDRESS,adder);
                params.put(KEY_CONTACT,cont);
                params.put(KEY_NAME,name);
                params.put(KEY_ORDER,orders);
                params.put(KEY_POSTCODE,postcode);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);
    }










    public void SessionUpdate(String contact,String name,String address)
    {
        SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "logged");
        editor.putString("contact", contact);
        editor.putString("address", address);
        editor.putString("name",name);
        editor.apply();
        Log.e("dsds",pref.getString("status",null)+"");


    }



}

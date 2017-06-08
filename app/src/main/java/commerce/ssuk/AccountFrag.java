package commerce.ssuk;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
public class AccountFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private Button sign,reg;
    private LinearLayout invoice;
    private TextView rep;
    private EditText repswd,pswd,contact,nam,order,addrs,name,usrlogin,pswdlogin;

    private static final String REGISTER_URL = "http://192.168.43.227:8000/api/register/";

    private static String urlJsonArry = "http://192.168.43.227:8000/api/login/";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_CONTACT = "contact";

    public static final String KEY_ORDER = "orders";

    public static final String KEY_NAME = "name";

    public static final String KEY_ADDRESS = "address";
    public void AccountFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
      View v=inflater.inflate(R.layout.account,container,false);
        repswd=(EditText)v.findViewById(R.id.repswd);
        pswd=(EditText)v.findViewById(R.id.pswd);
        usrlogin=(EditText)v.findViewById(R.id.edit_username0);
        pswdlogin=(EditText)v.findViewById(R.id.pswd0);
        contact=(EditText)v.findViewById(R.id.cont);
        addrs=(EditText)v.findViewById(R.id.address);
       nam=(EditText)v.findViewById(R.id.name);order=(EditText)v.findViewById(R.id.order);


        sign=(Button)v.findViewById(R.id.sign);reg=(Button)v.findViewById(R.id.register);


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUser();
            }
        });



        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pswd.getText().toString().trim().equals(repswd.getText().toString().trim()))
                {RegisterUser();}
                else Toast.makeText(getContext(),"Password mismatch",Toast.LENGTH_LONG).show();
            }
        });



return v;

    }








    private void RegisterUser(){
        final String repassword = repswd.getText().toString().trim();
        final String password = pswd.getText().toString().trim();

         //if(repassword!=password) return;

        final String name=nam.getText().toString().trim();

                final String addr= addrs.getText().toString().trim();
                        final String cont= contact.getText().toString().trim();
                                final String orders=order.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
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
                params.put(KEY_ADDRESS,addr);
                params.put(KEY_CONTACT,cont);
                params.put(KEY_NAME,name);
                params.put(KEY_ORDER,orders);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);
    }







    private void LogUser(){
        final String contact = usrlogin.getText().toString().trim();
        final String password = pswdlogin.getText().toString().trim();


        JsonObjectRequest req = new JsonObjectRequest(urlJsonArry+contact+"N"+password,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reps", response.toString());

                        try {


                           // JSONObject userObject = new JSONObject(response.toString());
                            Log.d("Reps", response.toString());

                            Toast.makeText(getContext(),
                                    "Successfully Logged In",
                                    Toast.LENGTH_LONG).show();


                            SessionUpdate(response.getString("contact"),
                                    response.getString("name"),response.getString("address"),response.getString("password"));








                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Check Username or Password",
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


    }

public void SessionUpdate(String contact,String name,String address,String password)
{
    SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("status", "logged");
    editor.putString("contact", contact);
    editor.putString("address", address);
    editor.putString("password", password);
    editor.putString("name",name);
    editor.apply();
    Log.e("dsds",pref.getString("status",null)+"");


}



}

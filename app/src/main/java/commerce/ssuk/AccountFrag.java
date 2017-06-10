package commerce.ssuk;

import android.content.SharedPreferences;import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
    private EditText repswd,pswd,contact,pin,nam,order,addrs,name,usrlogin,pswdlogin;

    private static final String pin_url = "http://192.168.43.227:8000/api/postcode/";

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
        usrlogin=(EditText)v.findViewById(R.id.edit_username0);
        pswdlogin=(EditText)v.findViewById(R.id.pswd0);
        pin=(EditText)v.findViewById(R.id.pin);



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

                RegisterUser();
            }
        });

try {
    SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode

    if (pref.getString("status", null).equals("logged")) {

        Log.e("dsdds",pref.getString("status",null)+"");
      Trans();
    }

}catch (Exception e)
{}



        return v;

    }
















    private void RegisterUser(){


        final String dest= pin.getText().toString();
        final String origin="58e32243eaf87011c22bc744";
        String urrr=pin_url+origin+"N"+dest;
        Log.e("Urlslfn",urrr);

        JsonObjectRequest req = new JsonObjectRequest(urrr,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reps", response.toString());

                        try {




                            if(Integer.parseInt(response.getString("value"))<=5)
                            {
                                Fragment fragment;
                                fragment=new register();
                                Bundle data= new Bundle();
                                data.putString("postcode",dest);
                                fragment .setArguments(data);


                                FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.r, fragment);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                            else
                            {Toast.makeText(getContext(),
                                        "Out of range of Shop",
                                        Toast.LENGTH_LONG).show();}







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
                                    response.getString("name"),response.getString("address"));


                          Trans();




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








public void Trans()
{
    Fragment fragment;
    fragment=new SettingsFrag();
    Bundle data= new Bundle();
    fragment .setArguments(data);

    FragmentTransaction transaction = ((FragmentActivity)getContext()
    ).getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.r, fragment);

    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    transaction.addToBackStack("name");
    transaction.commit();



}



}

package commerce.ssuk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by princes on 24-May-17.
 */
public class UpdateFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private Button pdsave,cpsave;
    private LinearLayout invoice;
    private CardView pers_card,pass_card;
    private EditText fname,lname,email,pswd,npwsd;

    private static final String FINAL_REGISTER_URL = "http://192.168.43.227:8000/api/pas/";

    private static final String FINAL_REGISTER_URL2 = "http://192.168.43.227:8000/api/reg/";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_CONTACT = "contact";
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ORDER = "orders";

    public static final String KEY_NAME = "name";

    private String postcode,orders;

    public static final String KEY_ADDRESS = "address";
    public static final String KEY_POSTCODE="postcode";

    public void UpdateFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_update,container,false);

        getActivity().invalidateOptionsMenu();

        pswd=(EditText) view.findViewById(R.id.pswd);
        npwsd=(EditText) view.findViewById(R.id.cpswd);
        email=(EditText) view.findViewById(R.id.email);
        fname=(EditText) view.findViewById(R.id.fname);
        lname=(EditText) view.findViewById(R.id.lname);
        cpsave=(Button) view.findViewById(R.id.cpsave);
        pdsave=(Button) view.findViewById(R.id.mails);


        pers_card=(CardView)view.findViewById(R.id.card_personal);
        pass_card=(CardView)view.findViewById(R.id.card_password);
        String category=getArguments().getString("category");
        pers_card.setVisibility(View.GONE);
        pass_card.setVisibility(View.GONE);
        SharedPreferences prefer = getContext().getSharedPreferences("session", 0); // 0 - for private mode

       try{ if(category.equals("personal"))

        {
             pers_card.setVisibility(View.VISIBLE);
            pass_card.setVisibility(View.GONE);
            String name[]=prefer.getString("name",null).split("#");
            fname.setText(name[1]);
            lname.setText(name[1]);
            email.setText(prefer.getString("email",null));
        }
        if(category.equals("password"))

        {
            pers_card.setVisibility(View.GONE);
            pass_card.setVisibility(View.VISIBLE);


        }}
       catch (Exception e)
       {
           Log.e("CAte","No value");

       }


       cpsave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode

               SharedPreferences.Editor editor = pref.edit();
               Log.e("saved password",pref.getString("password",null));
               Log.e("entered password",pswd.getText().toString().trim());

               Log.e("new password",npwsd.getText().toString().trim());
              if(((pref.getString("password",null)).equals(pswd.getText().toString().trim())))
               {
               if(!(pref.getString("password",null)).equals(npwsd.getText().toString().trim()))
               {
                   Log.e("naya",npwsd.getText().toString().trim());
                   update_password(pref.getString("contact", null), pref.getString("password", null), npwsd.getText().toString().trim());
               }}
               else
              {
                  Log.e("Sorry","cant");
              }
}
       });

        pdsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode

                personal_update(pref.getString("contact", null), pref.getString("password", null), fname.getText().toString()+"#"+lname.getText().toString(),email.getText().toString());



            }
        });



























        return view;



    }



    private void update_password(final String contact, final String password,final String name){

        orders="ff";postcode="dsd";
        final String addr="dsd";
        final  String mail="fsf";
        Log.e("Values",contact+orders+postcode+password+name+addr);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FINAL_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response); Toast.makeText(getContext(),obj.getString("ok"),Toast.LENGTH_LONG).show();
                            if(obj.getString("ok").equals("Password changed"))
                            { SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("password",name);
                                editor.commit();
                                Log.e("password",name);
                                Toast.makeText(getContext(),pref.getString("password",null),Toast.LENGTH_LONG).show();

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
                params.put(KEY_ADDRESS,addr);
                params.put(KEY_CONTACT,contact);
                params.put(KEY_NAME,name);
                params.put(KEY_ORDER,orders);
                params.put(KEY_POSTCODE,postcode);
                params.put(KEY_EMAIL,mail);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);
    }














    private void personal_update(final String contact, final String password,final String name,final String mail){

        orders="ff";postcode="dsd";final String address="sf";
        Log.e("Values",contact+orders+postcode+password+name+mail);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FINAL_REGISTER_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response); Toast.makeText(getContext(),obj.getString("ok"),Toast.LENGTH_LONG).show();
                            if(obj.getString("ok").equals("Password Changed"))
                            {
                                Toast.makeText(getContext(),"Updated",Toast.LENGTH_LONG).show();
                                SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("name",KEY_NAME);
                                editor.putString("email",KEY_EMAIL);
                                editor.commit();
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
                params.put(KEY_ADDRESS,address);
                params.put(KEY_CONTACT,contact);
                params.put(KEY_NAME,name);
                params.put(KEY_ORDER,orders);
                params.put(KEY_POSTCODE,postcode);

                params.put(KEY_EMAIL,mail);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);
    }








}

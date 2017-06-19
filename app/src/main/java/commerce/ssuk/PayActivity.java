package commerce.ssuk;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class PayActivity extends AppCompatActivity {

    String date,time,address,pincode,pay_id="null";
    Spinner month,year,type;
    private static final String CARD_URL = "http://192.168.43.227:8000/api/paycard/";
    public static final String KEY_DATE= "date";
    public static final String KEY_NAME = "name";

    public static final String KEY_CONTACT = "contact";

    public static final String KEY_PAY = "pay_id";

    public static final String KEY_ADDRESS = "bill_addr";

    public static final String KEY_NICK= "nick";

    public static final String KEY_NUMBER = "number";

    public static final String KEY_TYPE = "ctype";
    TextView sdate,stime,sadd,spin;

    private String[] arraySpinner={"1","2","3","4","5","6","7","8","9","10","11","12"};

    private String[] arraySpinne={"2017","2018","2019","2020","2021","2022","2023"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Bundle bundle = getIntent().getExtras();
  final Button save=(Button)findViewById(R.id.save);

        date = bundle.getString("date");
        time= bundle.getString("time");
        address = bundle.getString("address");
       pincode = bundle.getString("pin");
        final View success= findViewById(R.id.success);
        Button ckout=(Button)success.findViewById(R.id.addtime);
        final View payment=findViewById(R.id.payment);
        payment.setVisibility(View.GONE);
         sdate=(TextView)success.findViewById(R.id.vw);
        stime=(TextView)success.findViewById(R.id.vw1);
        sadd=(TextView)success.findViewById(R.id.vw2);
        spin=(TextView)success.findViewById(R.id.vw3);
        sdate.setText("Date : "+date);
        stime.setText("Time : "+time);
        sadd.setText("Address : "+address);
        spin.setText("Postcode : "+pincode);
        month=(Spinner)payment.findViewById(R.id.spinner1);

        year=(Spinner)payment.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinne);

        month.setAdapter(adapter);
        year.setAdapter(adapt);

        ckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success.setVisibility(View.GONE);
                payment.setVisibility(View.VISIBLE);


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_card("sd","ada","Adad","dad","Adad","dsd");
                    }
                });
            }
        });






















    }














    public void add_card(final String name, final  String nickname,final String address,final String type, final String date,final String number)
    {



        StringRequest stringRequest = new StringRequest(Request.Method.POST,CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getString("ok").equals("false"))
                            {     pay_id=obj.getString("pid");
                                Toast.makeText(PayActivity.this,"Card Added",Toast.LENGTH_LONG).show();
                                //Trans();

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
                        Toast.makeText(PayActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences pref = PayActivity.this.getSharedPreferences("session", 0); // 0 - for private mode


                params.put(KEY_NAME,name);
                params.put(KEY_TYPE,type);
                params.put(KEY_CONTACT,pref.getString("contact",null));
                params.put(KEY_NICK,nickname);
                params.put(KEY_ADDRESS,address);
                params.put(KEY_DATE,date);
                params.put(KEY_NUMBER,number);
                Log.e("param",params.toString());
                return params;


            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }






}




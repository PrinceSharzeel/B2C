package commerce.ssuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    TextView sdate,stime,sadd,error;
    EditText number,name,bill_add,nickname;
    LinearLayout paym;

    private String[] arraySpinner={"1","2","3","4","5","6","7","8","9","10","11","12"};

    private String[] arraySpinne={"2017","2018","2019","2020","2021","2022","2023"};

    private String[] arraySpin={"Debit Card","Credit Card"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Bundle bundle = getIntent().getExtras();
       Button save=(Button)findViewById(R.id.save);

        date = bundle.getString("date");
        time= bundle.getString("time");
        address = bundle.getString("address");
        pincode = bundle.getString("pin");
        type=(Spinner)findViewById(R.id.spinner);
        error=(TextView)findViewById(R.id.error);
        month=(Spinner)findViewById(R.id.spinner1);
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        final ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinne);
        final ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpin);
        error.setVisibility(View.GONE);
        year=(Spinner)findViewById(R.id.spinner2);
        month.setAdapter(adapter);
        year.setAdapter(adapt);
        type.setAdapter(adap);
        name=(EditText)findViewById(R.id.name);
        nickname=(EditText)findViewById(R.id.nick);
        number=(EditText)findViewById(R.id.number);
        bill_add=(EditText)findViewById(R.id.addr);






                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                            if(CardValidate.validate(number.getText().toString())) {
                                                                    if (Masker.NameCheck(name.getText().toString())){


                                                                        if(Masker.nickNameCheck(nickname.getText().toString()))
                                                                        {


                                                                            add_card(name.getText().toString(), nickname.getText().toString(), bill_add.getText().toString(), type.getSelectedItem().toString(), month.getSelectedItem().toString() + "/" + year.getSelectedItem().toString(), number.getText().toString());

                                                                              error.setVisibility(View.GONE);
                                                                            Intent in=new Intent(PayActivity.this,UploadOrder.class);
                                                                            startActivity(in);
                                                                            finish();





                                                                        }
                                                                        else{
                                                                            error.setVisibility(View.VISIBLE);
                                                                            error.setText("Invalid Nick Name");
                                                                        }






                                                                    }
                                                                    else{
                                                                        error.setVisibility(View.VISIBLE);
                                                                        error.setText("Invalid Name");


                                                                    }




                                            }
                                            else{
                                                error.setVisibility(View.VISIBLE);
                                                error.setText("Invalid Card Number");
                                            }

                    }
                });

















    }














    public void add_card(final String name, final  String nickname,final String address,final String type, final String date,final String number)
    {

        final ProgressDialog dialog = ProgressDialog.show(PayActivity.this, null, null);
        ProgressBar spinner = new android.widget.ProgressBar(PayActivity.this, null, android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();



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
                        error.printStackTrace();  dialog.dismiss();
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




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

public class SuccessActivity extends AppCompatActivity {

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
    EditText number,name,bill_add,nickname;
    LinearLayout paym;

    private String[] arraySpinner={"1","2","3","4","5","6","7","8","9","10","11","12"};

    private String[] arraySpinne={"2017","2018","2019","2020","2021","2022","2023"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        time= bundle.getString("time");
        address = bundle.getString("address");
        pincode = bundle.getString("pin");
        Button ckout=(Button)findViewById(R.id.addtime);
        sdate=(TextView)findViewById(R.id.vw);
        stime=(TextView)findViewById(R.id.vw1);
        sadd=(TextView)findViewById(R.id.vw2);
        spin=(TextView)findViewById(R.id.vw3);
        sdate.setText("Date : "+date);
        stime.setText("Time : "+time);
        if(address.equals("not"))
        {
            sadd.setText("Pick at the Store");
            spin.setText("");
            SharedPreferences pref = SuccessActivity.this.getSharedPreferences("DelVat", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("del","null");
            editor.apply();
        }
        else {

            sadd.setText("Address : " + address);
            spin.setText("Postcode : " + pincode);
        }


        ckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in=new Intent(SuccessActivity.this,PayActivity.class);
                Bundle data=new Bundle();
                data.putString("date",date);
                data.putString("time",time);
                data.putString("address",address);
                data.putString("pin",pincode);
                in.putExtras(data);
                startActivity(in);
                finish();

            }
        });




                

















    }




}




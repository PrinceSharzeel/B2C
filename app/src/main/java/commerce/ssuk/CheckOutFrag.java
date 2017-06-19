package commerce.ssuk;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static commerce.ssuk.AppController.TAG;
import static java.security.AccessController.getContext;

public class CheckOutFrag extends AppCompatActivity  {

    ImageButton btnDatePicker, btnTimePicker,btnAdress,adddd;
    CardView timecard,addcard,datecard;

    private static final String REGISTER_URL = "http://192.168.43.227:8000/api/order_add/";

    private static final String pin_url = "http://192.168.43.227:8000/api/postcode/";

    private static final String urlJsonArry = "http://192.168.43.227:8000/api/date_check/";
    public static final String KEY_DATE= "ord_date";
    public static final String KEY_ADDRESS = "ord_address";

    public static final String KEY_CONTACT = "contact";

    public static final String KEY_PAY = "pay_id";

    public static final String KEY_PINCODE = "pincode";

    public static final String KEY_TIME= "ord_time";

    public static final String KEY_ORDER = "order";

    public static final String KEY_STATUS = "status";
    String wstop,wstart,wdstop,wdstart;

    Button proceed;
    String odate="null";
    String oadd="null";
    String opin="null";
    String otime="null";
    Switch switchadd;
    String delivery;
    Boolean pin_test=false;

    TextView txtDate, txtyear,month,time,am,addr1,adr2,off;
    private int mYear, mMonth, mDay, mHour, mMinute;
    RelativeLayout input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checkout);
        Toolbar tb = (Toolbar)findViewById(R.id.toolBar);
        switchadd=(Switch)findViewById(R.id.switch1);

        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDatePicker=(ImageButton)findViewById(R.id.adddate);
        proceed=(Button)findViewById(R.id.button);
        adddd=(ImageButton)findViewById(R.id.chkState30);
        input=(RelativeLayout)findViewById(R.id.input);

        btnTimePicker=(ImageButton)findViewById(R.id.addtime);
        txtDate=(TextView) findViewById(R.id.date);
        txtyear=(TextView) findViewById(R.id.year);
        month=(TextView) findViewById(R.id.month);
        time=(TextView) findViewById(R.id.time);
        am=(TextView) findViewById(R.id.am);
        addr1=(TextView) findViewById(R.id.addr);
        adr2=(TextView) findViewById(R.id.adr);

        timecard=(CardView)findViewById(R.id.card_timedisp);
                addcard=(CardView)findViewById(R.id.card_add);
        datecard=(CardView)findViewById(R.id.card_datedisp);

        timecard.setVisibility(View.GONE);
        datecard.setVisibility(View.GONE);
        addcard.setVisibility(View.GONE);
        input.setVisibility(View.GONE);
        proceed.setVisibility(View.GONE);



        Intent intent = getIntent();
        delivery=intent.getStringExtra("delivery");
        Log.e("del",delivery);





        date_check("58e32243eaf87011c22bc744");
      off=(TextView)findViewById(R.id.off);



        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datecard.setVisibility(View.VISIBLE);
                                Log.e("dsd",year+"");
                                txtDate.setText(dayOfMonth+"");
                                txtyear.setText(year+"");
                                String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                                month.setText(MONTHS[monthOfYear]);
                                odate=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                procedd();
                                Log.e("day",odate+otime+oadd+opin);



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });







        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ds","clicked");


                PopupMenu popup = new PopupMenu(CheckOutFrag.this,btnTimePicker);
                //Inflating the Popup using xml file

                popup.getMenuInflater()
                        .inflate(R.menu.time_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        timecard.setVisibility(View.VISIBLE);
                        Log.e("ds",item.getTitle()+"");
                        String sel=item.getTitle()+"";
                        if(sel.equals("9 AM to 12 PM"))
                        {
                            time.setText("9 AM");
                            am.setText("to 12 PM");
                            otime="9 am to 12 pm"; procedd();

                            Log.e("time",odate+otime+oadd+opin);
                        }
                            else if(sel.equals("12 PM to 5 PM"))
                        {
                            time.setText("12 PM");
                            am.setText("to 5 PM");
                            otime="12 pm to 5 pm"; procedd();
                        }
                                else if(sel.equals("5 PM to 8 PM"))

                        {
                            time.setText("5 PM");
                            am.setText("to 8 PM");
                            otime="5 pm to 8 pm"; procedd();
                        }

                        return true;
                    }
                });

                popup.show();






            }
        });



        switchadd.setChecked(false);
        switchadd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    final AlertDialog.Builder alert = new AlertDialog.Builder(CheckOutFrag.this);

                    alert.setTitle("Home delivery");
                    alert.setMessage("Additional charge of "+delivery+" will be applicable.");
                    alert.setCancelable(false);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {


                            final AlertDialog.Builder alert = new AlertDialog.Builder(CheckOutFrag.this);
                            alert.setTitle("Pincode");
                            final EditText ionput = new EditText(CheckOutFrag.this);

                            ionput.setGravity(Gravity.CENTER_HORIZONTAL);
                            alert.setView(ionput);

                            alert.setCancelable(false);

                            alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {


                                    final String a = ionput.getText().toString().trim().toUpperCase();
                                    PinTest(a);
                                    if (!pin_test){

                                        if (!a.isEmpty()) {
                                            input.setVisibility(View.VISIBLE);
                                            adddd.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String b = ((EditText) findViewById(R.id.first0)).getText().toString().trim();
                                                    if (!b.isEmpty()) {
                                                        input.setVisibility(View.GONE);
                                                        addcard.setVisibility(View.VISIBLE);
                                                        addr1.setText(b);
                                                        adr2.setText(a);
                                                        opin = a;
                                                        oadd = b;

                                                        Log.e("add", odate + otime + oadd + opin);
                                                        procedd();
                                                    } else {
                                                        Toast.makeText(CheckOutFrag.this, "We can't deliver without an address,isn't it?", Toast.LENGTH_SHORT).show();

                                                    }


                                                }
                                            });
                                        } else {
                                            Toast.makeText(CheckOutFrag.this, "Pincode required", Toast.LENGTH_SHORT).show();
                                        }
                                }
                                else{

                                        final AlertDialog.Builder alert = new AlertDialog.Builder(CheckOutFrag.this);
                                        alert.setMessage("Sorry, we cannot deliver here!");

                                        alert.setCancelable(false);

                                        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {}});

                                    }


                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    switchadd.setChecked(false);

                                }
                            });
                            alert.show();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            switchadd.setChecked(false);

                        }
                    });
                    alert.show();



                }else{
                    input.setVisibility(View.GONE);
                    addcard.setVisibility(View.GONE);
                    opin="not";
                    oadd="not";
                    final AlertDialog.Builder alert = new AlertDialog.Builder(CheckOutFrag.this);
                    alert.setTitle("Pick at shop");
                    alert.setMessage("No delivery charges,You need to visit the shop to recieve.");

                    alert.setCancelable(false);
                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                    procedd();
                }

            }
        });

        //check the current state before we display the screen
        if(switchadd.isChecked()){


        }
        else {


        }






    }





    public void add_order_slot(final String contact, final  String postcode,final String adder,final String time, final String date)
    {  final String pay_id="null";
        final String orders="null";

        Log.e("vaues",date+time+adder+postcode+contact);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response); Toast.makeText(CheckOutFrag.this,obj.getString("ok"),Toast.LENGTH_LONG).show();
                            if(obj.getString("ok").equals("Slot Booked"))
                            {
                                Trans();

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
                        Toast.makeText(CheckOutFrag.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_TIME,time);
                params.put(KEY_ADDRESS,adder);
                params.put(KEY_CONTACT,contact);
                params.put(KEY_PAY,pay_id);
                params.put(KEY_ORDER,orders);
                params.put(KEY_PINCODE,postcode);
                params.put(KEY_DATE,date);
                params.put(KEY_STATUS,"slotted");
                Log.e("param",params.toString());
                return params;



            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("post",stringRequest+"");
        requestQueue.add(stringRequest);




    }



    public void procedd()
    {

        if(!(odate.equals("null")||otime.equals("null")||oadd.equals("null")||opin.equals("null"))) {
            proceed.setVisibility(View.VISIBLE);
            Log.e("values",odate+otime+oadd+opin);
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = CheckOutFrag.this.getSharedPreferences("session", 0); // 0 - for private mode


                        add_order_slot(pref.getString("contact",null),opin,oadd,otime,odate);

                }
            });


        }

    }






    public void Trans()
    {
        Intent in=new Intent(CheckOutFrag.this,PayActivity.class);
        Bundle data=new Bundle();
        data.putString("date",odate);
        data.putString("time",otime);
        data.putString("address",oadd);
        data.putString("pin",opin);
        in.putExtras(data);
        startActivity(in);

    }




    public void date_check(String value)
    {


        JsonObjectRequest req = new JsonObjectRequest(urlJsonArry+value,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject person) {
                        Log.d(TAG, person.toString());


                        try {
                            Log.e("fefefe", person.toString());



                            wstop = person.getString("wstop");
                                 wstart = person.getString("wstart");

                                 wdstop = person.getString("wdstop");

                               wdstart = person.getString("wdstart");
                            off.setText(" Choose between :\n Weekdays : "+wstart+" - "+wstop+"\n Weekends : "+wdstart+" - "+wdstop);






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckOutFrag.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(CheckOutFrag.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);








    }





    private void PinTest(final String dest){



        final String origin="58e32243eaf87011c22bc744";
        String urrr=pin_url+origin+"N"+dest;
        Log.e("Urlslfn",urrr);

        JsonObjectRequest req = new JsonObjectRequest(urrr,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reps", response.toString());

                        try {




                            if(Integer.parseInt(response.getString("value"))<=5) {
                                pin_test=true;
                            }






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckOutFrag.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Resp", "Error: " + error.getMessage());
                Toast.makeText(CheckOutFrag.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);






    }







}
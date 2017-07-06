package commerce.ssuk;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static commerce.ssuk.register.KEY_PASSWORD;


public class DeliveryAgent extends Fragment{

    private static final String Feed_URL = "http://192.168.43.227:8000/api/agent_order_update/";
    EditText oid,stat,feed;
    Button submit;
    String cont;
    public DeliveryAgent() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_deliveryagent, container, false);
        oid=(EditText)view.findViewById(R.id.oid);
        feed=(EditText)view.findViewById(R.id.feedback);
        stat=(EditText)view.findViewById(R.id.stat);
        submit=(Button)view.findViewById(R.id.submit);
        cont=getArguments().getString("contact");
        Toast.makeText(getContext(),cont,Toast.LENGTH_LONG).show();


           submit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                    SubmitData(oid.getText().toString(),stat.getText().toString(),feed.getText().toString());


               }
           });













        return view;
    }




public void  SubmitData(final String oid, final String stat, final String feed)
{




    final ProgressDialog dialog = ProgressDialog.show(getContext(), null, null);
    ProgressBar spinner = new android.widget.ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
    spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    dialog.setContentView(spinner);
    dialog.setCancelable(false);
    dialog.show();


    StringRequest stringRequest = new StringRequest(Request.Method.POST, Feed_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();


                    try {
                        JSONObject obj = new JSONObject(response); Toast.makeText(getContext(),obj.getString("ok"),Toast.LENGTH_LONG).show();
                        if(obj.getString("ok").equals("Order status updated"))
                        {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();dialog.dismiss();
                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();

            params.put("contact",cont);
            params.put("oid",oid);
            params.put("stat",stat);
            params.put("feed",feed);
            return params;
        }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    Log.e("post",stringRequest+"");
    requestQueue.add(stringRequest);







}






















}
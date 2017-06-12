package commerce.ssuk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by princes on 24-May-17.
 */
public class AddressFrag  extends Fragment{
    private ImageButton edit;
    private EditText address,post;

    private static final String pin_url = "http://192.168.43.227:8000/api/postcode/";
    public void AddressFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_address,container,false);

        getActivity().invalidateOptionsMenu();


        address=(EditText)view.findViewById(R.id.address);
        post=(EditText)view.findViewById(R.id.pincode);

        edit=(ImageButton)view.findViewById(R.id.edit);
       edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addrs=post.getText().toString();
                Pattern pattern = Pattern.compile("^(((\\+44\\s?\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$");
                Matcher matcher = pattern.matcher(addrs);

                if (matcher.matches()) {
                    Log.e("valid","Phone Number Valid");
                }
                else
                {
                    Log.e("no-valid","Phone Number must be in the form XXX-XXXXXXX");
                }

            }
        });




















        return view;



    }













    private void RegisterUser(){


        final String dest= post.getText().toString();
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








}

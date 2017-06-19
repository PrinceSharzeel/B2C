package commerce.ssuk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.password;




/**
 * Created by prince on 19/6/17.
 */





public class Masker {


    public String pincode_mask(String pin, final Context context)
    {








        JsonObjectRequest req = new JsonObjectRequest("http://api.postcodes.io/postcodes/"+pin+"/validate",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reps", response.toString());



                        try {


                            // JSONObject userObject = new JSONObject(response.toString());
                            Log.d("Repscccccc", response.toString());
                            if((response.getString("result")).equals("true"))
                            {


                                AppController.postvalye ="true";

                            }
                            else{

                                AppController.postvalye ="false";

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,
                                    "Invalid Postcode",
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Resp", "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(req);



        return  AppController.postvalye;





    }


    public String  contactcheck(String u)
    {


        return "fd";
    }






    public boolean passwordValidation(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}"
        );
        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }





    public boolean LoginCheck(Context context)
    {
        SharedPreferences pref=context.getSharedPreferences("session",0);
        String chk=pref.getString("status",null);
        return chk.equals("logged");

    }



















}

package commerce.ssuk;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class FavFragment extends Fragment {
    private RecyclerView recyclerView;

    private static String urlJsonArry = "http://192.168.43.227:8000/api/category/58e32243eaf87011c22bc744/";

    Context context;
    private List<Options> albumList;
    private CategoryAdapter adapter;
    CardView placard;
    public FavFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.catergory_list, container, false);

        context=getContext();

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        getActivity().invalidateOptionsMenu();
        placard=(CardView)view.findViewById(R.id.placard);
        albumList = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), albumList);
        NestedScrollView scrollView = (NestedScrollView)view.findViewById(R.id.nsv);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        placard.setVisibility(View.GONE);
           StoreOpenclose(view);
//Parsing the Rest API to fetch category list of products
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("pcat");
                              //  String email = person.getString("pdisc");

                                Options a=new Options(name,"");
                                albumList.add(a);
                                Log.e("resultsuij",name);

                                adapter.notifyDataSetChanged();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);





        return view;
    }



    public void StoreOpenclose(final View view)
    {


//Parsing the Rest API to fetch category list of products
        JsonObjectRequest req = new JsonObjectRequest( "http://192.168.43.227:8000/api/store/58e32243eaf87011c22bc744",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                       TextView det;

                        try {

                            det=(TextView)view.findViewById(R.id.low);
                            Log.d("Responffffse", response.getString("status"));


                            String name = response.getString("status");

                            if(name.equals("open"))
                            {
                                placard.setVisibility(View.GONE);

                            }
                            else{
                                Log.e("repsdps",response.toString());

                                placard.setVisibility(View.VISIBLE);
                                det.setText("Store Closed \n Till : "+response.getString("time")+" \n "+response.getString("msg"));

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);




    }














}
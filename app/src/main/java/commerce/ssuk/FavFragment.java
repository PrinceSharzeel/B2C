package commerce.ssuk;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class FavFragment extends Fragment{
    private RecyclerView recyclerView;

    private static String urlJsonArry = "http://192.168.43.227:8000/api/category";

    Context context;
    private List<Options> albumList;
    private CategoryAdapter adapter;

    public FavFragment() {
        // Required empty public constructor
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

        albumList = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), albumList);
        NestedScrollView scrollView = (NestedScrollView)view.findViewById(R.id.nsv);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);




/*

        Options a = new Options("Organic",R.drawable.car1);
        Options a1 = new Options("Fresh Food",R.drawable.car2);
        Options a2 = new Options("Frozen Food",R.drawable.car2);
        Options a3 = new Options("Dairy",R.drawable.car1);
        Options a4 = new Options("Poultry",R.drawable.car2);
        albumList.add(a);
        albumList.add(a1);
        albumList.add(a2);
        albumList.add(a);albumList.add(a);albumList.add(a3);albumList.add(a);albumList.add(a4);albumList.add(a);albumList.add(a);







        adapter.notifyDataSetChanged();*/




//Parsing the Rest API
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














}
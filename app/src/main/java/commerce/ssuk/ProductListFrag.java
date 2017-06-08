package commerce.ssuk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static commerce.ssuk.AppController.TAG;
import static commerce.ssuk.AppController.getInstance;

/**
 * Created by princes on 24-May-17.
 */
public class ProductListFrag  extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Item> albumList;
    Context context;
    private static String urlJsonArry = "http://192.168.43.227:8000/api/products/";


    public void ProductListFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.productlistfrag,container,false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().setIcon();
        //Recycler
        getActivity().invalidateOptionsMenu();

        context=getContext();
        String value = getArguments().getString("category");
        albumList=new ArrayList<>();
// Listener to ADD button in product list
        adapter = new ProductAdapter(albumList, new ProductAdapter.MyAdapterListener() {
            @Override
            public void AddCartViewOnClick(View v, int position) {
                String TAG="Dwdw";
                Log.d(TAG, "iconTextViewOnClick at position "+position);
                Log.e("Tag",albumList.get(position).getName()+"");


                final DBAdapter db = new DBAdapter(getContext());
                try {
                    db.open();
                    db.insertCart(albumList.get(position).getName() + "", albumList.get(position).getNumOfSongs() + "");

                    Toast.makeText(getActivity(), "Inserted",
                            Toast.LENGTH_SHORT).show();

                    db.close();
                    getActivity().invalidateOptionsMenu();
                }
                catch (Exception e){Log.e("Db","Ds");}










            }

        });












        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
       //int spancount= (int) Math.ceil( data.size() / 3f )
        RecyclerView.LayoutManager mLayoutmanager=new GridLayoutManager(getContext(),2);




        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);






//Parsing the Rest API
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry+value,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("ptitle");
                                String email = person.getString("price");

                                Item a=new Item(name,email,R.drawable.bg);
                                albumList.add(a);
                                Log.e("resultsuij",name);

                                adapter.notifyDataSetChanged();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);





















        return view;



    }




}

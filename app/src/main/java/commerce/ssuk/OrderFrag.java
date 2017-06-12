package commerce.ssuk;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
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

import static commerce.ssuk.AppController.TAG;

/**
 * Created by princes on 24-May-17.
 */
public class OrderFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private TextView pr;
    public static String profilepic;
    private Button checkout,checkoutt;
    private LinearLayout invoice;
   private  String value;
    long price,delivery,total,savings;

    private static String urlJsonArry ="http://192.168.43.227:8000/api/prod_detail/";
    public void OrderFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.trolley,container,false);
        albumList = new ArrayList<>();
        adapter = new TrolleyAdapter(getContext(), albumList);
        empty =(Button)view.findViewById(R.id.testbutton);
        checkout =(Button)view.findViewById(R.id.cancelButton);
        invoice =(LinearLayout) view.findViewById(R.id.invoice);
        checkoutt =(Button)view.findViewById(R.id.checkout0);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        pr=(TextView) view.findViewById(R.id.price);
        getActivity().invalidateOptionsMenu();


        try{
        final DBAdapter db = new DBAdapter(getContext());
        db.open();
        Cursor c = db.getAllCart();
            price=0;

            int i=0;
        if (c.moveToFirst())
        {
            do {
                 ParsePic(c.getString(1));
                int cost=Integer.parseInt(c.getString(2))*Integer.parseInt(c.getString(3));

                Item it =new Item(c.getString(1),"€ "+cost,profilepic,c.getString(3));
                price=price+cost;


                albumList.add(it);
                i++;




            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
        db.close();
        Log.e("itne h total",i+"");}
        catch (Exception e){
            Log.e("Db","Ds");}



        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().invalidateOptionsMenu();

                EmptyTrolley(); albumList = new ArrayList<>();
                adapter = new TrolleyAdapter(getContext(), albumList);recyclerView.setAdapter(adapter);
            }
        });


        pr.setText("Price : € "+price+"");









        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent in = new Intent(getContext(),CheckOutFrag.class);
                startActivity(in);


            }
        });
















        return view;



    }




  public void EmptyTrolley()
  {
      final DBAdapter db = new DBAdapter(getContext());
      db.open();
      db.deleteAllItems();
      Toast.makeText(getActivity(), "Trolley Emptied",Toast.LENGTH_SHORT).show();
      db.close();

  }






  public  void ParsePic(final String value)
  {


      JsonArrayRequest req = new JsonArrayRequest(urlJsonArry+value,
              new Response.Listener<JSONArray>() {
                  @Override
                  public void onResponse(JSONArray response) {
                      Log.d("Reps", response.toString());

                      try {

                          final JSONObject person = (JSONObject) response
                                  .get(0);

                          profilepic = person.getString("ppro");







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

      AppController.getInstance().addToRequestQueue(req);








  }




}

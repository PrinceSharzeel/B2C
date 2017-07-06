package commerce.ssuk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import static commerce.ssuk.AppController.Global_Contact;
import static commerce.ssuk.AppController.TAG;

/**
 * Created by princes on 24-May-17.
 */
public class OrderFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private TextView pr,dl,vt,to;
    public static String profilepic;
    private Button checkout,checkoutt;
    private LinearLayout invoice;
    private ProgressBar pb;
   private  String value;
   float price=0,delivery=0,total,vat=0,red=0,p=0;


    private static String urlJsonArry ="http://192.168.43.227:8000/api/prod_detail/";

    private static String urldel ="http://192.168.43.227:8000/api/delvat/";
    Masker ob;
    public void OrderFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);

 ob=new Masker();

   if(AppController.popcount==0&&ob.LoginCheck(getActivity())) {
     ///Notification to alert trolley items limit of 3 days
       AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.InvitationDialog);

       builder.setTitle("Trolley Session");
       builder.setMessage("Record of trolley items will be maintained for 3 days only.You need to order within it.");
       builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

           public void onClick(DialogInterface dialog, int which) {
               // Do nothing but close the dialog

               dialog.dismiss();
           }
       });
       AlertDialog alert = builder.create();
       alert.show();
       AppController.popcount=1;
   }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        final View view=inflater.inflate(R.layout.trolley,container,false);
        albumList = new ArrayList<>();
        empty =(Button)view.findViewById(R.id.testbutton);
        checkout =(Button)view.findViewById(R.id.cancelButton);
        invoice =(LinearLayout) view.findViewById(R.id.invoice);
        checkoutt =(Button)view.findViewById(R.id.checkout0);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pb=(ProgressBar)view.findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);







 // adds del vat and total price charges toview
        adapter = new TrolleyAdapter(getContext(),albumList, new TrolleyAdapter.MyAdapterListener() {
            @Override
            public void AddCartViewOnClick(final View v, final int position) {

               red= Float.parseFloat(albumList.get(position).getNumOfSongs().substring(2));
                Log.e("###########3######",red+"");
                if(red!=0)
                {
                    Log.e("#####232323###",price+"");
                    price=price-red;
                    Log.e("########rfwef3######",price+"");
                    pr.setText("Price : € "+price+"");
                    if(adapter.getItemCount()>0)
                    {delvat("58e32243eaf87011c22bc744",red);}
                    else{
                        pr.setText("Price :");
                        dl.setText("Delivery Charge :");
                        vt.setText("VAT :");
                        to.setText("Total :");
                        price=0;
                        total=0;

                    }



                }


            }

        });









        recyclerView.setAdapter(adapter);


        pr=(TextView) view.findViewById(R.id.price);

        dl=(TextView) view.findViewById(R.id.delivery);

        vt=(TextView) view.findViewById(R.id.vat);

        to=(TextView) view.findViewById(R.id.total);
        getActivity().invalidateOptionsMenu();






 // Fethces products from sqlite db and adds to List
        try{
        final DBAdapter db = new DBAdapter(getContext());
        db.open();
            SharedPreferences pref =getContext().getSharedPreferences("session",0);

            Cursor c = db.getAllCart(pref.getString("contact",null));
            price=0;
            Log.e("contageettt",pref.getString("contact",null));

            int i=0;
        if (c.moveToFirst())
        {
            do {

                Float cost=Float.parseFloat(c.getString(2))*Float.parseFloat(c.getString(3));
                Log.e("time",c.getString(5));

                Item it =new Item(c.getString(1),"€ "+cost,c.getString(6),c.getString(3));
                price=price+cost;
                pb.setVisibility(View.VISIBLE);


                albumList.add(it);
                i++;




            } while (c.moveToNext());
        }
            pb.setVisibility(View.GONE);

            adapter.notifyDataSetChanged();
        db.close();
        Log.e("itne h total",i+"");}
        catch (Exception e){
            Log.e("Db","Ds");}


  // removes all the items and empty the Cart db for the current user
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().invalidateOptionsMenu();
                pr.setText("Price :");
                dl.setText("Delivery Charge : € 0");
                vt.setText("VAT :");
                to.setText("Total :");

                price=0;
                total=0;

                EmptyTrolley(); albumList = new ArrayList<>();
                adapter = new TrolleyAdapter(getContext(), albumList);recyclerView.setAdapter(adapter);
            }
        });

        Log.e("va",delivery+"/"+vat);
          if(adapter.getItemCount()>0)
          {delvat("58e32243eaf87011c22bc744",0);}

        Log.e("val",delivery+"/"+vat);
        pr.setText("Price : € "+price+"");


        final SharedPreferences pref =getContext().getSharedPreferences("session",0);

// proceed to order by slot choice

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pref.contains("status")) {



                    if(pref.getString("status",null).equals("logged")) {

                         // required to have minimum price order
                                if (price < 40.00) {
                                    Toast.makeText(getContext(),
                                            "Minimum order should be of 40 €",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Log.e("delivery",delivery+"");
                                Intent in = new Intent(getContext(), CheckOutFrag.class).putExtra("delivery",delivery+"");
                                startActivity(in);


                    }
                    else
                    { ViewPager viewPager = (ViewPager) getActivity().findViewById(
                            R.id.viewpager);
                        viewPager.setCurrentItem(4);

                    }


                }
                else
                {
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(
                            R.id.viewpager);
                    viewPager.setCurrentItem(4);
                }


            }
        });
















        return view;



    }



/// Empty trolley
  public void EmptyTrolley()
  {
      final DBAdapter db = new DBAdapter(getContext());
      db.open();

      SharedPreferences pref=getActivity().getSharedPreferences("session",0);
      db.deleteAllItems(pref.getString("contact",null));
      Toast.makeText(getActivity(), "Trolley Emptied",Toast.LENGTH_SHORT).show();
      db.close();

  }



//fetch del vat


  public  void delvat(final String value,float reduct)
  {


      JsonObjectRequest req = new JsonObjectRequest(urldel+value,null,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                      Log.d("Reps", response.toString());

                      try {



                          delivery = Long.parseLong(response.getString("del_char"));
                         vat = Long.parseLong(response.getString("vat"));

                          Log.e("fln",vat+" "+delivery+" "+price);

                          delVat_session(vat+"",price+"",delivery+"");

                          Log.e("valeeee",delivery+"/"+vat);
                          dl.setText("Delivery Charge : € 0");
                          vt.setText("VAT : "+vat+" %");
                          p=price;
                          vat=p*vat/100;
                          p=vat+p;
                          p=p+delivery;
                          to.setText("Total : € "+p);







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








// Store del vat in shared pref for later use

    public void delVat_session(String vat,String price,String del)
    {
        SharedPreferences pref = getContext().getSharedPreferences("DelVat", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("price",price);
        editor.putString("del", del);
        editor.putString("vat", vat);
        editor.apply();
        Log.e("valeesdsee",pref.getString("vat",null)+"/"+vat);


    }






}

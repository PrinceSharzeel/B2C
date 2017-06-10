package commerce.ssuk;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by princes on 24-May-17.
 */
public class OrderFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private Button checkout,checkoutt;
    private LinearLayout invoice;
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


        getActivity().invalidateOptionsMenu();


        //Recycler View


        try{
        final DBAdapter db = new DBAdapter(getContext());
        db.open();
        Cursor c = db.getAllCart();

      //  if(db.NumberOfItems()==0){invoice.setVisibility(View.GONE);checkout.setVisibility(View.GONE);empty.setVisibility(View.GONE);checkoutt.setText("Trolley is empty");}

            int i=0;
        if (c.moveToFirst())
        {
            do {
               // Toast.makeText(getContext(),"id: " + c.getString(0) + " \n" +"Name: " + c.getString(1) + "\n" +
               //                 "Email: " + c.getString(2),
                //        Toast.LENGTH_LONG).show();
                Item it =new Item(c.getString(1),"Rs."+c.getString(2),"/static/media/Nutella.png");
                albumList.add(it);
                //db.deleteContact(Integer.parseInt(c.getString(0)));
                i++;




            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
        db.close();
        Log.e("itne h total",i+"");}    catch (Exception e){Log.e("Db","Ds");}



        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().invalidateOptionsMenu();

                EmptyTrolley(); albumList = new ArrayList<>();
                adapter = new TrolleyAdapter(getContext(), albumList);recyclerView.setAdapter(adapter);
            }
        });


























        return view;



    }

  public void EmptyTrolley()
  {

      final DBAdapter db = new DBAdapter(getContext());
      db.open();
      db.deleteAllItems();

      Toast.makeText(getActivity(), "Trolley Emptied",
              Toast.LENGTH_SHORT).show();

      db.close();
  }



}

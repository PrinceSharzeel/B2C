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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by princes on 24-May-17.
 */
public class SettingsFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private Button checkout,checkoutt;
    private LinearLayout invoice;
    private CardView pd,sign,addrp,cp,setting;
    public void SettingsFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.settingsfrag,container,false);

        getActivity().invalidateOptionsMenu();

        pd=(CardView)view.findViewById(R.id.pd);
        sign=(CardView)view.findViewById(R.id.sign);
        cp=(CardView)view.findViewById(R.id.cp);
        addrp=(CardView)view.findViewById(R.id.addp);
        setting=(CardView)view.findViewById(R.id.setting);





        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                new AlertDialog.Builder(v.getContext())
                        .setTitle("Sign out?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                SharedPreferences pref = getContext().getSharedPreferences("session", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("status", "loggedout");
                                editor.remove("contact");
                                editor.remove("address");
                                editor.remove("name");
                                editor.remove("password");
                                editor.apply();
                                Log.e("dsds",pref.getAll()+"");
                                getFragmentManager().popBackStack();


                            }})
                        .setNegativeButton(android.R.string.no, null).show();






            }
        });



        pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment=new UpdateFrag();
                Bundle data= new Bundle();
                data.putString("category","personal");
                fragment .setArguments(data);


                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fcl, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment=new UpdateFrag();
                Bundle data= new Bundle();
                data.putString("category","password");
                fragment .setArguments(data);


                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fcl, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });



        addrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment=new AddressFrag();
                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fcl, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });



















        return view;



    }






}

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
public class SettingsFrag  extends Fragment{
    private RecyclerView recyclerView;
    private TrolleyAdapter adapter;
    private List<Item> albumList;
    private Button empty;
    private Button checkout,checkoutt;
    private LinearLayout invoice;
    public void SettingsFrag(){}




    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.settingsfrag,container,false);

        getActivity().invalidateOptionsMenu();


        //Recycler View



        return view;



    }

  




}

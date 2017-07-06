package commerce.ssuk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class TrolleyAdapter extends RecyclerView.Adapter<TrolleyAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Item> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public Button add;
        public ImageButton close;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            add=(Button) view.findViewById(R.id.add);
            close=(ImageButton)view.findViewById(R.id.close);

            add=(Button) view.findViewById(R.id.add);







        }





    }




    public TrolleyAdapter(Context mContext, List<Item> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public TrolleyAdapter(Context mContext,List<Item> albumList, MyAdapterListener listener) {

        this.albumList=albumList;
        onClickListener = listener;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trolleyitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs());
//Log.e(album.getName()+'');
        // loading album cover using Glide library
//        Log.e("urrr",album.getThumbnail());
        Picasso.with(mContext).load("http://192.168.43.227:8000"+album.getThumbnail()).into(holder.thumbnail);

        //details of the product in the trolley
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(v.getContext(),prod_view.class);
                in.putExtra("Name",album.getName());
                v.getContext().startActivity(in);
            }
        });
    // removess a specific item from trolley
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DBAdapter db=new DBAdapter(v.getContext());
                db.open();
                SharedPreferences pref=mContext.getSharedPreferences("session",0);
                db.deleteContact(album.getName(),pref.getString("contact",null));
                Toast.makeText(mContext, "Item deleted",
                        Toast.LENGTH_SHORT).show();
                db.close();
                ((HomActivity)mContext).invalidateOptionsMenu();
                onClickListener.AddCartViewOnClick(v, position);
                Log.e("dbaao","haan");


                albumList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, albumList.size());


            }
        });


        holder.add.setText(album.getQuant());


    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {

        void AddCartViewOnClick(View v, int position);
    }


}


class Orders {
    private String name,price,pro,disc,redprice,units,img;
    public Orders() {
    }

    public Orders(String name,String price,String pro,String disc,String redprice,String units,String img) {
        this.name = name;
        this.img=img;

        this.pro= pro;
        this.price =price;
        this.disc=disc;
        this.redprice=redprice;
        this.units=units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price =price;
    }


    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }



    public String getRedprice() {
        return redprice;
    }

    public void setRedprice(String redprice) {
        this.redprice =redprice;
    }
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
    public String getImg(){ return  img;}
    public void setImg(String img){this.img=img;}











}
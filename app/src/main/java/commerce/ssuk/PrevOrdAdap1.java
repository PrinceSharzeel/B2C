package commerce.ssuk;

/**
 * Created by prince on 22/6/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PrevOrdAdap1 extends RecyclerView.Adapter<PrevOrdAdap1.MyViewHolder>  {

    private Context mContext;
    private List<PrevObs> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,date,price,status;
        public ImageView signal;
        public Button quantity;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            signal = (ImageView) view.findViewById(R.id.signal);
            quantity=(Button) view.findViewById(R.id.quant);

            price = (TextView) view.findViewById(R.id.price);

            status= (TextView) view.findViewById(R.id.count);










        }





    }




    public PrevOrdAdap1(Context mContext, List<PrevObs> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public PrevOrdAdap1(Context mContext,List<PrevObs> albumList, MyAdapterListener listener) {

        this.albumList=albumList;
        onClickListener = listener;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prev_ord_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PrevObs album = albumList.get(position);

        holder.title.setText(album.getName());
        holder.status.setText(album.getDisc());
        holder.price.setText(album.getPrice());
        holder.quantity.setText(album.getUnit());

        holder.date.setText(album.getDate());
         if(album.getDisc().equals("slotted"))
             Picasso.with(mContext).load(R.drawable.progress).into(holder.signal);

         else
             Picasso.with(mContext).load(R.drawable.delivered).into(holder.signal);


        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Tapped",album.getName());
                Fragment fragment;
                fragment=new PrevOrder2();
                Bundle data= new Bundle();
                data.putString("ord_id",album.getIds());
                fragment .setArguments(data);


                FragmentTransaction transaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frep, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

//        Picasso.with(mContext).load(R.drawable.clock).into(holder.signal);








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



class PrevObs {
    private String name,exp,price, unit,disc,ids;
    private int thumbnail;

    public  PrevObs() {
    }

    public  PrevObs(String name,String exp,String price,String unit,String disc,int thumbnail,String ids) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.exp = exp;
        this.price =price;this.unit=unit;
        this.disc = disc;
        this.ids=ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return exp;
    }

    public void setDate(String exp) {
        this.exp = exp;
    }


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids=ids;
    }
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit=unit;
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














}

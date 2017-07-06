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

public class PrevOrdAdap2 extends RecyclerView.Adapter<PrevOrdAdap2.MyViewHolder>  {

    private Context mContext;
    private List<PrevObs2> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,date,price,status;
        public ImageView prof;
        public Button quantity;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.count);
            prof= (ImageView) view.findViewById(R.id.thumbnail);
            quantity=(Button) view.findViewById(R.id.add);










        }





    }




    public PrevOrdAdap2(Context mContext, List<PrevObs2> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public PrevOrdAdap2(Context mContext,List<PrevObs2> albumList, MyAdapterListener listener) {

        this.albumList=albumList;
        onClickListener = listener;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prev_ord_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PrevObs2 album = albumList.get(position);

        holder.title.setText(album.getName());
        holder.price.setText(album.getNumOfSongs());
        holder.quantity.setText(album.getQuant());
        Picasso.with(mContext).load("http://192.168.43.227:8000"+album.getThumbnail()).into(holder.prof);



        holder.prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Tapped","inside quant");

                Intent in=new Intent(v.getContext(),prod_view.class);
                in.putExtra("Name",album.getName());
                v.getContext().startActivity(in);
            }
        });









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



class PrevObs2 {
    private String name;
    private String numOfSongs;
    private String thumbnail;
    private String quant;

    public PrevObs2() {
    }

    public PrevObs2(String name, String numOfSongs, String thumbnail, String quant) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
        this.quant = quant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(String numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }
}











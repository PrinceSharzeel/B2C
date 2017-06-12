package commerce.ssuk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Orders> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,disc,redprice;
        public ImageView thumbnail;
        public Button add;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            disc = (TextView) view.findViewById(R.id.desc);

            redprice = (TextView) view.findViewById(R.id.dis);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            add=(Button) view.findViewById(R.id.add);

/*
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.AddCartViewOnClick(v, getAdapterPosition());


                }
            });  */


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(mContext,add);
                    //Inflating the Popup using xml file

                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            onClickListener.AddCartViewOnClick(v, getAdapterPosition(),item.getTitle()+"");
                            add.setText(item.getTitle());

                            return true;
                        }
                    });

                    popup.show(); //showing popup menu
                }
            }); //closing the setOnClickListener method










            }





    }




    public ProductAdapter(Context mContext, List<Orders> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public ProductAdapter(Context mContext,List<Orders> albumList, MyAdapterListener listener) {

        this.mContext = mContext;
        this.albumList=albumList;
        onClickListener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productcard, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Orders album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText("€ "+album.getPrice());
        holder.redprice.setText("€ "+album.getRedprice());
        holder.redprice.setPaintFlags(holder.redprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.disc.setText(album.getDisc());
        Picasso.with(mContext).load("http://192.168.43.227:8000"+album.getPro()).into(holder.thumbnail);



        // loading album cover using Glide library
        //Picasso.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(v.getContext(),ProductDetail.class);
                in.putExtra("Name",album.getName());
                in.putExtra("Price",album.getPrice());
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

        void AddCartViewOnClick(View v, int position,String a);
    }


}







class Products {
    private String name,exp,category, brand ,price, unit,litres ,weight,packge,min,max,disc,pro;
    private String thumbnail;

    public Products() {
    }

    public Products(String name,String exp,String category,String brand,String price,String unit,String litres,String weight,String packge,String min,String max,String disc,String pro,String thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.exp = exp;this.category= category;
        this.brand =brand;
        this.price =price;
        this.packge = packge;
        this.pro = pro;this.min= min;this.max = max;this.unit=unit; this.litres = litres;this.weight = weight;
        this.disc = disc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category= category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand =brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price =price;
    }


    public String getPackge() {
        return packge;
    }

    public void setPackge(String packge) {
        this.packge = packge;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min= min;
    }
    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit=unit;
    }


    public String getLitres() {
        return litres;
    }

    public void setLitres(String litres) {
        this.litres = litres;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }














}
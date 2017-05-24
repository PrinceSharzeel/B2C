package commerce.ssuk;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;


public class FavFragment extends Fragment{
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Item> albumList;
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.car1, R.drawable.car2,R.drawable.car3};

    private ImageView trans;

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_main, container, false);



        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), albumList);
        trans=(ImageView)view.findViewById(R.id.trans);

        NestedScrollView scrollView = (NestedScrollView)view.findViewById(R.id.nsv);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        carouselView = (CarouselView)view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);



        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView.setImageListener(imageListener);


        prepareAlbums();

        try {
            //Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
            Picasso.with(getContext())
                    .load("http://cdn2.stylecraze.com/wp-content/uploads/2014/03/3697-Slogans-On-Healthy-Food.jpg")
                    .resize(50, 50)
                    .centerCrop()
                    .into(trans);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return view;
    }




    private void prepareAlbums() {
        int[] covers = new int[]{
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,};

        Item a = new Item("Organic", "13", covers[0]);
        albumList.add(a);

        a = new Item("Stored Food","13", covers[1]);
        albumList.add(a);

        a = new Item("Dairy Products", "13", covers[2]);
        albumList.add(a);

        a = new Item("Drinks", "13", covers[3]);
        albumList.add(a);


        adapter.notifyDataSetChanged();
    }




    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }









}
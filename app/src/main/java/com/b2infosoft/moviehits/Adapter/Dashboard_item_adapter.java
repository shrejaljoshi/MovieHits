package com.b2infosoft.moviehits.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.b2infosoft.moviehits.Interface.MovieItemClickListener;
import com.b2infosoft.moviehits.Pojo.Dashboard_item;
import com.b2infosoft.moviehits.R;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.b2infosoft.moviehits.Useful.ApiConstant.BaseUrl;


public class Dashboard_item_adapter extends RecyclerView.Adapter<Dashboard_item_adapter.MyViewHolder> {

    public static ImageView image_Movie;
    private Context mContext;
    private List<Dashboard_item> albumList;
    MovieItemClickListener movieItemClickListener;
    String strMovieTitle = "",strMovieRating = "",strReleaseDate = "",strMovieDescription = "";
    String url = "";

    public Dashboard_item_adapter(Context mContext, List<Dashboard_item> albumList,MovieItemClickListener listener) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.movieItemClickListener = listener;



    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_row_item, parent, false);
        return new MyViewHolder(itemView);
   }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Dashboard_item album = albumList.get(position);
        holder.tv_MovieName.setText(album.getMovieTitle());


         url = BaseUrl+album.getMovieImage();
        System.out.println("==url=====image========"+url.toString());

        Glide.with(mContext).load(url)
                .thumbnail(Glide.with(mContext).load(R.drawable.preloader)).into(holder.image_Movie);

        strMovieTitle = album.getMovieTitle();
        strMovieRating = album.getRating();
        strReleaseDate = album.getMovieReleaseDate();
        strMovieDescription = album.getMovieDescription();

        ViewCompat.setTransitionName(holder.image_Movie, album.getMovieImage());




        holder.itemView.setTranslationY((-50 + position * 100));
        holder.itemView.setAlpha(0.5f);
        holder.itemView.animate().alpha(1f).translationY(0).setDuration(1000).start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            movieItemClickListener.onAnimalItemClick( position, album, holder.image_Movie,  mContext.getResources().getString(R.string.sharedImageView));
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView tv_MovieName;
        public static ImageView image_Movie;


        public MyViewHolder(View view) {
            super(view);

            tv_MovieName = view.findViewById(R.id.tvMovieName);
            image_Movie = view.findViewById(R.id.imgMovie);

        }


    }



}

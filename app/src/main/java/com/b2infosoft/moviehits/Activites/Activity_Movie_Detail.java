package com.b2infosoft.moviehits.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.b2infosoft.moviehits.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class Activity_Movie_Detail extends AppCompatActivity {



    Context mContext;
    TextView tv_movie_title,tv_ratings,tv_releaseDate,tv_movie_description;
    ImageView imgMovie,imgClose;

    String strMovieTitle = "",strMovieRating = "",strReleaseDate = "",strMovieDescription = "";
    String url = "";
    RevealAnimation mRevealAnimation;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_detail);
        mContext = Activity_Movie_Detail.this;
        supportPostponeEnterTransition();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Movie Hits");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_movie_title =findViewById(R.id.tv_movie_title);
        imgMovie = findViewById(R.id.imgMovie);
        tv_ratings = findViewById(R.id.tv_ratings);
        tv_releaseDate =findViewById(R.id.tv_releaseDate);
        tv_movie_description = findViewById(R.id.tv_movie_description);




        toolbar.setNavigationIcon(R.drawable.ic_cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





       Intent  intent = getIntent();
       if(intent != null)
       {
           strMovieTitle = intent.getStringExtra("movieTitle");
           url = intent.getStringExtra("movieimg");
           strMovieRating = intent.getStringExtra("movieRatings");
           strReleaseDate = intent.getStringExtra("movieReleaseDate");
           strMovieDescription = intent.getStringExtra("movieDescription");

       }

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        mRevealAnimation = new RevealAnimation(rootLayout,intent, this);

        initView(strMovieTitle,url,strMovieRating,strReleaseDate,strMovieDescription);



    }


    private void initView(String strMovieTitle, String url, String strMovieRating, String strReleaseDate,String strMovieDescription)
    {

        tv_movie_title.setText(strMovieTitle);
        tv_ratings.setText(mContext.getString(R.string.star)+" "+strMovieRating);
        tv_releaseDate.setText(strReleaseDate);
        tv_movie_description.setText(strMovieDescription);



        System.out.println("==url=====image====get===="+url.toString());

        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()

                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imgMovie);

    }


    @Override
    public void onBackPressed() {

        mRevealAnimation.unRevealActivity();

    }
}



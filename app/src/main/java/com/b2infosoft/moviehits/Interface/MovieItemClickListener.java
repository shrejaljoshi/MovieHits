package com.b2infosoft.moviehits.Interface;

import android.widget.ImageView;

import com.b2infosoft.moviehits.Pojo.Dashboard_item;

public interface MovieItemClickListener {
    void onAnimalItemClick(int pos, Dashboard_item movieItem, ImageView shareImageView,String sharedImageview);
}

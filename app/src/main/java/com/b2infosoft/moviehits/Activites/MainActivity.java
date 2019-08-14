package com.b2infosoft.moviehits.Activites;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.b2infosoft.moviehits.Adapter.CustomPagerDasboardAdapter;
import com.b2infosoft.moviehits.Adapter.Dashboard_item_adapter;
import com.b2infosoft.moviehits.Interface.MovieItemClickListener;
import com.b2infosoft.moviehits.Pojo.Dashboard_item;
import com.b2infosoft.moviehits.R;
import com.b2infosoft.moviehits.Useful.GridSpacingItemDecorationDashboard;
import com.b2infosoft.moviehits.Webservice.WebServiceCaller;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.b2infosoft.moviehits.Useful.ApiConstant.BaseUrl;
import static com.b2infosoft.moviehits.Useful.ApiConstant.CategroryApi;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.PERMISSIONS;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.PERMISSION_ALL;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.hasPermissions;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.isNetworkAvaliable;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.showToast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MovieItemClickListener {

    public  static  DrawerLayout drawer;


    NavigationView navigationView;
    Context mContext;
    Dashboard_item_adapter dashboard_item_adapter;
    List<Dashboard_item> dashboard_items;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView toolbar_title;

    ViewPager viewPager;
    int images[] = {R.drawable.dash1, R.drawable.dash2, R.drawable.dash1, R.drawable.dash2};
    com.b2infosoft.moviehits.Adapter.CustomPagerDasboardAdapter CustomPagerDasboardAdapter;
    SwipeRefreshLayout pullToRefresh;
    String apikey ="";

    String strMovieTitle = "",strMovieRating = "",strReleaseDate = "",strMovieDescription = "";
    String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.viewPager);
        recyclerView =findViewById(R.id.recyclerview);
        apikey =  "dd7f8940ddc78d1db23b2add56f00ec0";
        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Movie Hits");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(R.id.toolbar, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }


        toolbar.setNavigationIcon(R.drawable.ic_nav);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        pullToRefresh=findViewById(R.id.pullToRefresh);
        dashboard_item_adapter = new Dashboard_item_adapter(mContext, dashboard_items,this);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            int Refreshcounter = 1;

            @Override
            public void onRefresh() {

                dashboard_item_adapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });



        getCategory();

        //  calculateNoOfColumns();
        CustomPagerDasboardAdapter = new CustomPagerDasboardAdapter(mContext, images);
        viewPager.setAdapter(CustomPagerDasboardAdapter);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % images.length);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);
        dashboard_items = new ArrayList<>();


    }
    private void initRecycleView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationDashboard(3, dpToPx(0), true, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dashboard_item_adapter = new Dashboard_item_adapter(mContext, dashboard_items,this);
        recyclerView.setAdapter(dashboard_item_adapter);

    }

    private void getCategory() {
        if (isNetworkAvaliable(mContext)) {
            WebServiceCaller webServiceCaller = new WebServiceCaller(WebServiceCaller.GET_TASK, mContext, "Please Wait...", true) {
                @Override
                public void handleResponse(String response) throws JSONException {
                    System.out.println("=======response==========" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray resultArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject movieObj = resultArray.getJSONObject(i);
                            dashboard_items.add(new Dashboard_item(movieObj.getString("id"),
                                    movieObj.getString("vote_average"),
                                    movieObj.getString("title"),
                                    movieObj.getString("poster_path"),
                                    movieObj.getString("overview"),
                                    movieObj.getString("release_date")
                            ));
                            initRecycleView();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            webServiceCaller.addNameValuePair("api_key", apikey);
            webServiceCaller.execute(CategroryApi);
        }else {
            showToast(mContext, mContext.getString(R.string.you_are_not_connected_to_internet));

        }
    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.exit_from_application));
        builder.setMessage(R.string.exit_app)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAnimalItemClick(int pos, Dashboard_item movieItem, ImageView shareImageView, String transition) {

        int revealX = (int) (shareImageView.getX() + shareImageView.getWidth() / 2);
        int revealY = (int) (shareImageView.getY() + shareImageView.getHeight() / 2);

        Intent intent = new Intent(this, Activity_Movie_Detail.class);
        intent.putExtra("movieTitle", movieItem.getMovieTitle());
        intent.putExtra("movieRatings",movieItem.getRating());
        intent.putExtra("movieReleaseDate",movieItem.getMovieReleaseDate());
        intent.putExtra("movieDescription",movieItem.getMovieDescription());
        intent.putExtra("movieimg",BaseUrl+movieItem.getMovieImage());
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X,revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y,revealY);


        Pair<View, String> mPair1 = new Pair<View, String>(shareImageView, ViewCompat.getTransitionName(shareImageView));

       // ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, mPair1);
      //  ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, shareImageView, getString(R.string.sharedImageView));

        overridePendingTransition(0, 0);

        startActivity(intent);


    }
}

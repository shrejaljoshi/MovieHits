package com.b2infosoft.moviehits.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.b2infosoft.moviehits.Adapter.CustomPagerDasboardAdapter;
import com.b2infosoft.moviehits.Adapter.Dashboard_item_adapter;
import com.b2infosoft.moviehits.Pojo.Dashboard_item;
import com.b2infosoft.moviehits.R;
import com.b2infosoft.moviehits.Useful.GridSpacingItemDecorationDashboard;
import com.b2infosoft.moviehits.Webservice.WebServiceCaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.b2infosoft.moviehits.Activites.MainActivity.drawer;
import static com.b2infosoft.moviehits.Useful.ApiConstant.CategroryApi;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.PERMISSIONS;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.PERMISSION_ALL;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.hasPermissions;



public class Fragment_Dashboard extends Fragment  {

    Dashboard_item_adapter dashboard_item_adapter;
    List<Dashboard_item> dashboard_items;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView toolbar_title;
    Context mContext;
    ViewPager viewPager;
    int images[] = {R.drawable.dash1, R.drawable.dash2, R.drawable.dash1, R.drawable.dash2};
 CustomPagerDasboardAdapter CustomPagerDasboardAdapter;
    SwipeRefreshLayout pullToRefresh;
    String apikey ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = getActivity();
        viewPager = view.findViewById(R.id.viewPager);
        recyclerView = view.findViewById(R.id.recyclerview);
        apikey =  "dd7f8940ddc78d1db23b2add56f00ec0";
        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
        }

        toolbar = view.findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);

        toolbar_title.setText(mContext.getString(R.string.menu_home));
        toolbar.setNavigationIcon(R.drawable.ic_nav);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        pullToRefresh=view.findViewById(R.id.pullToRefresh);
    //    dashboard_item_adapter = new Dashboard_item_adapter(mContext, dashboard_items);
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
        CustomPagerDasboardAdapter = new CustomPagerDasboardAdapter(getActivity(), images);
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

        return view;


    }


    private void initRecycleView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationDashboard(3, dpToPx(0), true, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      //  dashboard_item_adapter = new Dashboard_item_adapter(mContext, dashboard_items,this);
        recyclerView.setAdapter(dashboard_item_adapter);

    }

    private void getCategory() {

        WebServiceCaller webServiceCaller = new WebServiceCaller(WebServiceCaller.GET_TASK, mContext, "Please Wait...", true) {
            @Override
            public void handleResponse(String response) throws JSONException {
                System.out.println("=======response=========="+response);
                try {
                JSONObject jsonObject = new JSONObject(response);

                   JSONArray resultArray = jsonObject.getJSONArray("results");
                    for(int i=0;i<resultArray.length();i++)
                    {
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
        webServiceCaller.addNameValuePair("api_key",  apikey);
        webServiceCaller.execute(CategroryApi);

    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}




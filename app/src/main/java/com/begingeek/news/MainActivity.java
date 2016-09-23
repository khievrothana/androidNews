package com.begingeek.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.begingeek.news.adapter.FeedListAdapter;
import com.begingeek.news.app.AppController;
import com.begingeek.news.model.News;
import com.begingeek.news.utils.EndlessScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private View loadinFooter;
    private FeedListAdapter listAdapter;
    private List<News> newsItems;
    private String URL_FEED = "http://begingeek.com/api/v1/news?page=";
    private int PageNumber = 0;
    private int TotalNumber = 1;
    private int FirstVisibleItem, VisibleItemCount, TotalItemCount;
    private boolean isLoading = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        newsItems = new ArrayList<News>();
        listAdapter = new FeedListAdapter(this, newsItems);
        loadinFooter = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress_layout, null, false);
        listView.setAdapter(listAdapter);
        customLoadMoreDataFromApi();
        PageNumber=1;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                int lastItem = FirstVisibleItem + VisibleItemCount;
                if(lastItem==TotalItemCount && !isLoading && PageNumber < TotalNumber)
                {
                    listView.addFooterView(loadinFooter);
                    Log.i("PageNumber", PageNumber+"");
                    Log.i("TotalPages", TotalNumber+"");
                    customLoadMoreDataFromApi();
                    isLoading =true;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FirstVisibleItem = firstVisibleItem;
                VisibleItemCount = visibleItemCount;
                TotalItemCount = totalItemCount;
            }
        });

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String strId = ((TextView)view.findViewById(R.id.id)).getText().toString();
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("strId", strId);
                startActivity(i);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsItems = new ArrayList<News>();
                listAdapter = new FeedListAdapter(activity, newsItems);
                listView.setAdapter(listAdapter);
                PageNumber=0;
                customLoadMoreDataFromApi();
                PageNumber++;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void customLoadMoreDataFromApi(){
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED+PageNumber);
        if(entry!=null){
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED+PageNumber, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    parseJsonFeed(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
        }
    }

    private void parseJsonFeed(JSONObject response) {
        try{
            boolean isError = response.getBoolean("IsError");
            if(!isError){
                JSONObject data = response.getJSONObject("Data");
                TotalNumber = data.getInt("Pages");
                JSONArray news = data.getJSONArray("News");
                for (int i =0;i<news.length();i++){
                    JSONObject newsObj = news.getJSONObject(i);
                    String Id = newsObj.getString("Id");
                    String Title = newsObj.getString("Title");
                    String FeaturImage = newsObj.getString("FeatureImage");
                    String DateCreated = newsObj.getString("DateCreated").split(" ")[0];

                    News item = new News(Id,Title,FeaturImage,DateCreated);
                    newsItems.add(item);
                }

                if(isLoading){
                    isLoading = false;
                    listView.removeFooterView(loadinFooter);
                    PageNumber++;
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                listAdapter.notifyDataSetChanged();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

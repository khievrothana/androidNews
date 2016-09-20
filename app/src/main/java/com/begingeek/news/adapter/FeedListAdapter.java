package com.begingeek.news.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.begingeek.news.R;
import com.begingeek.news.app.AppController;
import com.begingeek.news.listviewfeed.FeedImageView;
import com.begingeek.news.model.News;

import java.util.List;

/**
 * Created by itrot on 9/8/2016.
 */
public class FeedListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<News> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<News> news){
        this.activity =activity;
        this.feedItems = news;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int i) {
        return feedItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.row_front_feed, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView dateCreated = (TextView) view.findViewById(R.id.dateCreated);
        FeedImageView feedImageView = (FeedImageView) view.findViewById(R.id.feedImage1);

        News item = feedItems.get(i);
        name.setText(item.getTitle());
        dateCreated.setText(item.getDateCreated());

        // Chcek for empty status message
//        if (!TextUtils.isEmpty(item.getStatus())) {
//            statusMsg.setText(item.getStatus());
//            statusMsg.setVisibility(View.VISIBLE);
//        } else {
//            // status is empty, remove from view
//            statusMsg.setVisibility(View.GONE);
//        }

        // Checking for null feed url
//        if (item.get() != null) {
//            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//                    + item.getUrl() + "</a> "));
//
//            // Making url clickable
//            url.setMovementMethod(LinkMovementMethod.getInstance());
//            url.setVisibility(View.VISIBLE);
//        } else {
//            // url is null, remove from the view
//            url.setVisibility(View.GONE);
//        }

        // user profile pic
       // profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Feed image
        if (item.getFeatureImage() != null) {
            feedImageView.setImageUrl(item.getFeatureImage(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return view;
    }
}

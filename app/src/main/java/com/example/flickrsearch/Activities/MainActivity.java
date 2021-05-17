/**
 *
 */
package com.example.flickrsearch.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrsearch.Adapters.FlickrRecycleViewAdapter;
import com.example.flickrsearch.Data.PhotoData;
import com.example.flickrsearch.Interfaces.OnDataAvailable;
import com.example.flickrsearch.Interfaces.OnRecyclerClickListener;
import com.example.flickrsearch.Listeners.RecyclerItemClickListener;
import com.example.flickrsearch.R;
import com.example.flickrsearch.Utils.DownloadStatus;
import com.example.flickrsearch.Utils.GetFlickrJsonData;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnDataAvailable, OnRecyclerClickListener {
    private static final String TAG = "MainActivity";
    private FlickrRecycleViewAdapter recycleViewAdapter;
    static boolean onInit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        if (onInit) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferences.edit().putString(FLICKR_QUERY, "").apply();
            onInit = false;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(this,recyclerView,this));
        recycleViewAdapter = new FlickrRecycleViewAdapter(new ArrayList<PhotoData>(),this);
        recyclerView.setAdapter(recycleViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main,menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu created");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: start");
        GetFlickrJsonData flickrJsonData = new GetFlickrJsonData("en-us",true,this);
//        flickrJsonData.executeOnSameThread("android,oreo");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryTags = sharedPreferences.getString(FLICKR_QUERY,"");
        if(queryTags.length()>0){
            flickrJsonData.execute(queryTags);
        }
        Log.d(TAG, "onResume: ends");
    }


    @Override
    public void OnItemClickListener(View view, int position) {
        PhotoData photoData = recycleViewAdapter.getPhotoData(position);
        if (photoData != null) {
            Intent intent = new Intent(this, PhotoDetailActivity.class);
            intent.putExtra(PHOTO_TRANSFER, photoData);
            startActivity(intent);
        }
    }


    @Override
    public void onDataAvailable(ArrayList<PhotoData> data, DownloadStatus status) {
        if(status==DownloadStatus.OK){
//            Log.d(TAG, "onDataAvailable: data is: "+data);
            recycleViewAdapter.loadNewData(data);
        }else {
            Log.e(TAG, "onDataAvailable: download failed with status: "+status);

        }
    }
}
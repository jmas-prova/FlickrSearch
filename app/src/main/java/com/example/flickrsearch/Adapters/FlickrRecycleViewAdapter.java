/**
 *
 */
package com.example.flickrsearch.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flickrsearch.Data.PhotoData;
import com.example.flickrsearch.R;

import java.util.ArrayList;

public class FlickrRecycleViewAdapter extends RecyclerView.Adapter<FlickrRecycleViewAdapter.FlickrImageViewHolder>{
    private static final String TAG = "FlickrRecycleViewAdapt";
    private ArrayList<PhotoData> photoDataArrayList;
    private Context context;


    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called when view manager need a new view
        Log.d(TAG, "onCreateViewHolder: New View requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,parent,false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        //called by the layout manager when it needs new data in an existing row
        if(photoDataArrayList==null || photoDataArrayList.size()==0){
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText("");
            holder.author.setText(R.string.error404);
        }else {
            PhotoData photoItem = photoDataArrayList.get(position);
            Log.d(TAG, "onBindViewHolder:" + photoItem.getTitle() + " * " +photoItem.getAuthor() + "--->" + position);
            Glide.with(context)
                    .load(photoItem.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .optionalCenterCrop()
                    )
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
            holder.author.setText(photoItem.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: called");

        return (((photoDataArrayList!=null)&&(photoDataArrayList.size()!=0)) ? photoDataArrayList.size() : 1);
    }

    public void loadNewData(ArrayList<PhotoData> newphotoData){
        Log.d(TAG, "loadNewData: new data requested");
        photoDataArrayList =newphotoData;
        notifyDataSetChanged();
     }

    public PhotoData getPhotoData(int position){
        // returns the photo data of the picture that is being tapped using its index number
        return ( (photoDataArrayList!=null) && (photoDataArrayList.size()>0) ? photoDataArrayList.get(position) : null) ;
    }

    public FlickrRecycleViewAdapter(ArrayList<PhotoData> PhotoDataArrayList, Context Context) {
        photoDataArrayList = PhotoDataArrayList;
        context = Context;
    }

    static class  FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title =null;
        TextView author =null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: start");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.author = (TextView) itemView.findViewById(R.id.author);
        }
    }
}

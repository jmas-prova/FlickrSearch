/**
 *
 */
package com.example.flickrsearch.Listeners;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrsearch.Interfaces.OnRecyclerClickListener;

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    private final OnRecyclerClickListener mlistener;
    private final GestureDetectorCompat mgestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        this.mlistener = listener;
        this.mgestureDetector = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView != null && mlistener != null){
                    Log.d(TAG, "onSingleTapUp: calling listener: listener.onItemClick");
                    mlistener.OnItemClickListener(childView,recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mgestureDetector!=null){
            boolean result= mgestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned: "+result);
            return result;
        }else {
            Log.d(TAG, "onInterceptTouchEvent: returned: false");
            return false;
        }
//        return super.onInterceptTouchEvent(rv, e);
    }
}

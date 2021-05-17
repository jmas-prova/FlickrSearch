/**
 *
 */
package com.example.flickrsearch.Interfaces;

import com.example.flickrsearch.Data.PhotoData;
import com.example.flickrsearch.Utils.DownloadStatus;

import java.util.ArrayList;

public interface OnDataAvailable {
    void onDataAvailable(ArrayList<PhotoData> data, DownloadStatus status);
}

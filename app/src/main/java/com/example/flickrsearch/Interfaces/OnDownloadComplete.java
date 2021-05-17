/**
 *
 */
package com.example.flickrsearch.Interfaces;

import com.example.flickrsearch.Utils.DownloadStatus;

public interface OnDownloadComplete {
    void onDownloadComplete(String data, DownloadStatus status);
}

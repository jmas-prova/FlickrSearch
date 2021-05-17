/**
 *
 */
package com.example.flickrsearch.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flickrsearch.BuildConfig;
import com.example.flickrsearch.Data.PhotoData;
import com.example.flickrsearch.R;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PhotoDetailActivity extends BaseActivity {
    private static final String TAG = "PhotoDetailActivity";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        PhotoData photoData =(PhotoData) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photoData != null){
            imageView =  findViewById(R.id.PhotoView);
            Glide.with(this)
                    .load(photoData.getBig_img_url())
                    .apply(new RequestOptions()
                            .error(R.drawable.placeholder)
                    )
                    .into(imageView);

            TextView title =findViewById(R.id.Photo_title);
            title.setText(photoData.getTitle());

            TextView author = findViewById(R.id.photoAuthor);
            author.setText(photoData.getAuthor());

            TextView dateTaken = findViewById(R.id.dateTaken);
            String strDateTaken="";
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                strDateTaken = LocalDateTime.parse(photoData.getDateTaken().substring(0,19)).format(dtf);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dateTaken.setText(strDateTaken);

            TextView description = findViewById(R.id.description);
            description.setText(Html.fromHtml(photoData.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_detail,menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu created");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_share){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            shareImage(bitmap);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareImage(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/png");
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}

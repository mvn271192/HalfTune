package com.example.manikandanvnair.halftune;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import Models.Track;

public class MainActivity extends AppCompatActivity {

    static String BaseURL = "https://itunes.apple.com/search?media=music&entity=song&term=";

    ArrayList<Track> tracks = new ArrayList<Track>();
    RecyclerView trackListView;
    TrackListAdapter trackListAdapter;
    private File direct;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AndroidNetworking.initialize(getApplicationContext());
// Adding an Network Interceptor for Debugging purpose
//
 if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        // Do the file write
    createDirectory();

 } else {
        // Request permission from the user
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
     ActivityCompat.requestPermissions(this,
             new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

    }


         trackListAdapter = new TrackListAdapter(this, tracks, new RecyclerViewClicks() {
             @Override
             public void onItemSelected(int position, View v) {

             }

             @Override
             public void onButtonClicked(int position, View cell, Button button) {

                 Track selectedTrack = tracks.get(position);
                 downloadMusic(selectedTrack,cell);

             }

             @Override
             public void onLongClicked(int position) {

             }
         });
//
        trackListView = (RecyclerView) findViewById(R.id.trackListView);
        trackListView.setLayoutManager(new LinearLayoutManager(this));
        trackListView.setAdapter(trackListAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                    createDirectory();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTuneFromText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
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

    public void createDirectory()

    {
        if(isExternalStorageReadable() && isExternalStorageWritable())
        {
            Toast.makeText(this,"Storage is ok",Toast.LENGTH_SHORT).show();
            direct = new File(Environment.getExternalStorageDirectory()+"/HalfTune");
            if(!direct.exists()) {
                if(direct.mkdir()){
                    //directory is created;
                    Toast.makeText(this,"Directory created",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"Directory not created",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void searchTuneFromText(String text)
    {
        trackListAdapter.getData().clear();
        String searchText = "";
        try {
            searchText = URLEncoder.encode(text,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String searchURL  = BaseURL + searchText;

        AndroidNetworking.post(searchURL)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.println(Log.INFO,"Half", response.toString());
                        formatResult(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.println(Log.INFO,"Half", error.toString());
                    }
                });
    }

    public void formatResult(JSONObject response)
    {
        try {
            JSONArray result = response.getJSONArray("results");
            for(int i=0;i<result.length();i++)
            {
                JSONObject item = (JSONObject) result.get(i);
                String previewUrl = item.getString("previewUrl");
                String trackName = item.getString("trackName");
                String artistName = item.getString("artistName");
                String artUrl = item.getString("artworkUrl100");
                String trackId = item.getString("trackId");

                Track track = new Track(trackName,artistName,previewUrl,artUrl,trackId);
                tracks.add(track);


            }

            reloadAllData(tracks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reloadAllData(ArrayList<Track> newList){

        trackListAdapter.getData().addAll(newList);
        trackListAdapter.notifyDataSetChanged();
    }

    private void downloadMusic(Track track, View view)
    {

        AndroidNetworking.download(track.getPreviewUrl(),direct.getPath(),track.getTrackId() + ".m4a")
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .setPercentageThresholdForCancelling(50) // even if at the time of cancelling it will not cancel if 50%
                .build()                                 // downloading is done.But can be cancalled with forceCancel.
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                        Toast.makeText(MainActivity.this,Long.toString(bytesDownloaded),Toast.LENGTH_LONG).show();
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        Toast.makeText(MainActivity.this,"Download complete",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.i("HalfTune",error.getLocalizedMessage());
                    }
                });
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


}

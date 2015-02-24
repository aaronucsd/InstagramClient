package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "efb7087faa0641c1b0592743ea97d38a";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //Send out api request to popular photos on instagram
        photos = new ArrayList<>();

        //1. Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);

        //2. Find the ListView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        //3. Set the adapter binding it to the ListView
        lvPhotos.setAdapter(aPhotos);

        // Fetch the popular photos
        fetchPopularPhotos();
    }

    /* Trigger API request
    - https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
         (JSON response - Get a list of what media is most popular at the moment. Can return mix of image and video types.)
         - Response
            type: {“data” => [x] => “type” } (“image” or “video”)
            url: {“data” => [x] => “images”  => “standard_resolution” => “url"}
            caption: {“data” => [x] => “caption”  => “text"}
            Author Name: {“data” => [x] => “user”  => “username"}
      */
    public void fetchPopularPhotos(){
        String  url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;// for read and write -> access_token=ACCESS-TOKEN
        // create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        //trigger the get request
        client.get(url, null, new JsonHttpResponseHandler(){
            // onSuccess (200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {//JSONObject as JSON root is object, not array
               // Expecting a JSON object (dictionary object)
               Log.i("DEBUG", response.toString());

               // Iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data");//array of posts
                    // iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++){
                        // get the json object at that position in the array
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributes of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();

                        // Author Name: {“data” => [x] => “user”  => “username"}
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        // caption: {“data” => [x] => “caption”  => “text"}
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        // type: {“data” => [x] => “type” } (“image” or “video”)
                        //photo.type = photoJSON.getJSONObject("type").getString("text");
                        // url: {“data” => [x] => “images”  => “standard_resolution” => “url"}
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        // Likes count
                        photo.likeCount = photoJSON.getJSONObject("likes").getInt("count");

                        //user profile image
                        photo.userProfileImage = photoJSON.getJSONObject("user").getString("profile_picture");

                        //(Optional) relative timestamp, like count [x], user profile image

                        // Add decode object to the photos array list
                        photos.add(photo);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

                // Callback - After we retrieve the photos above, we want to notify the ListView and Adaptor to do a refresh
                aPhotos.notifyDataSetChanged();
            }

            // onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // DO SOMETHING
            }
        });//async - sends as background process
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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
}

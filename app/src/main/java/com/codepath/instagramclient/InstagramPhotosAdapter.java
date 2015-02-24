package com.codepath.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by long on 2/19/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{
    // What data do we need from the activity
    // Context, Data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //what our item looks like
    // Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);
        // Check if we are using a recycled view, if not we need to inflate
        if(convertView == null){
            // Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);//false = do not attach to parent just yet
        }
        // Lookup the views for populating the data (image, caption etc)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView stUsername = (TextView) convertView.findViewById(R.id.stUsername);
        ImageButton ibProfileImage = (ImageButton) convertView.findViewById(R.id.ibProfileImage);
        TextView stLikes = (TextView) convertView.findViewById(R.id.stLikes);

        // Insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        stUsername.setText(photo.username);
        stLikes.setText(photo.likeCount+" likes");

        // Clear our the last imageview
        ivPhoto.setImageResource(0);
        // Insert the actual image using picasso with the url - some slow background
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);


        //with rounded corners using RoundedImageView
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(photo.userProfileImage).fit().transform(transformation).into(ibProfileImage);


        // Return the created item as a view
        return convertView;
    }
}

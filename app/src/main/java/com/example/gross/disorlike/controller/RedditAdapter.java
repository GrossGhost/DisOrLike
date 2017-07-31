package com.example.gross.disorlike.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gross.disorlike.R;
import com.example.gross.disorlike.model.SubredditResponseGson.PictureInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RedditAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private LayoutInflater inflater;
    private List<PictureInfo> pictureList;
    private String lastLoadedItem;

    public RedditAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.pictureList = new ArrayList<>();
        this.lastLoadedItem = "";
    }

    public void addItem (PictureInfo pictureItem){
        this.pictureList.add(pictureItem);
    }

    public String getlastLoadedItem(){
        return this.lastLoadedItem;
    }
    public void setlastLoadedItem(String lastItem){
        this.lastLoadedItem = lastItem;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = inflater.inflate(R.layout.item_reddit,parent, false);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(pictureList.get(position).getThumbnail())
                .into(holder.redditImage);
        holder.redditText.setText(pictureList.get(position).getTitle());
        holder.scoreText.setText(pictureList.get(position).getScore());
        holder.dislikeImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        holder.likeImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);

        Boolean likes = pictureList.get(position).getLikes();
        Log.d("LIKES", ""+likes);
        if (likes != null ){
            if (likes){
                holder.likeImage.setImageResource(R.drawable.ic_thumb_up_green_24dp);
            }else{
                holder.dislikeImage.setImageResource(R.drawable.ic_thumb_down_red_24dp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.pictureList.size();
    }

}

class RecyclerViewHolders extends RecyclerView.ViewHolder{

    ImageView redditImage, likeImage, dislikeImage;
    TextView redditText, scoreText;

    RecyclerViewHolders(View itemView) {
        super(itemView);
        redditImage = (ImageView) itemView.findViewById(R.id.image_reddit);
        likeImage = (ImageView) itemView.findViewById(R.id.image_like);
        dislikeImage = (ImageView) itemView.findViewById(R.id.image_dislike);
        redditText = (TextView) itemView.findViewById(R.id.text_reddit);
        scoreText = (TextView) itemView.findViewById(R.id.text_score);

    }
}

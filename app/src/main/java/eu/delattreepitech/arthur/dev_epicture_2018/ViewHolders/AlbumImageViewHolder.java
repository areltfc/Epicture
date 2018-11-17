package eu.delattreepitech.arthur.dev_epicture_2018.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import eu.delattreepitech.arthur.dev_epicture_2018.TextViews.MontserratTextView;

public class AlbumImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView _image;
    public VideoView _video;
    public MontserratTextView _text;

    public AlbumImageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}

package eu.delattreepitech.arthur.dev_epicture_2018.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;

import eu.delattreepitech.arthur.dev_epicture_2018.Types.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.ViewHolders.ImageViewHolder;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
import eu.delattreepitech.arthur.dev_epicture_2018.Activities.Album;

public class BaseAdapter extends RecyclerView.Adapter {
    private Activity _context;
    private List<Image> _images;
    private User _user;

    public BaseAdapter(Activity context, List<Image> images, User user) {
        _context = context;
        _images = images;
        _user = user;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageViewHolder vh = new ImageViewHolder(_context.getLayoutInflater().inflate(R.layout.image, null));
        vh._image = vh.itemView.findViewById(R.id.photo);
        vh._text = vh.itemView.findViewById(R.id.title);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
        final Image image = _images.get(i);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailed = new Intent(_context, Album.class);
                detailed.putExtra("image", new Gson().toJson(image));
                detailed.putExtra("user", new Gson().toJson(_user));
                _context.startActivity(detailed);
            }
        };
        imageViewHolder._text.setText(Image.cropName(image.getName()));
        imageViewHolder._text.setTextColor(_context.getColor(R.color.white));
        Glide.with(_context)
                .load("https://i.imgur.com/" + image.getId() + ".gif")
                .apply(new RequestOptions()
                        .fitCenter())
                .into(imageViewHolder._image);
        imageViewHolder._image.setOnClickListener(listener);
        imageViewHolder._text.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return _images.size();
    }
}

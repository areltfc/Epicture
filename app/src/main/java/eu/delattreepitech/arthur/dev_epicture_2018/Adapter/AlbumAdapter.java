package eu.delattreepitech.arthur.dev_epicture_2018.Adapter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.User;
import eu.delattreepitech.arthur.dev_epicture_2018.ViewHolder.ImageViewHolder;

public class AlbumAdapter extends RecyclerView.Adapter {
    private AppCompatActivity _context;
    private List<Image> _album;
    private User _user;

    public AlbumAdapter(AppCompatActivity context, List<Image> album, User user) {
        _context = context;
        _album = album;
        _user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageViewHolder vh = new ImageViewHolder(_context.getLayoutInflater().inflate(R.layout.album_image_template, null));
        vh._image = vh.itemView.findViewById(R.id.image);
        vh._text = vh.itemView.findViewById(R.id.text);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ImageViewHolder vh = (ImageViewHolder) viewHolder;
        final Image image = _album.get(i);
        if (image.getType().equals("mp4")) {
            ConstraintLayout layout = _context.findViewById(R.id.album_constraint);
            VideoView vv = new VideoView(_context.getBaseContext());
            Uri uri = Uri.parse("https://i.imgur.com/" + image.getRealId() + ".mp4");
            vv.setVideoURI(uri);
            vv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            vv.start();
            layout.addView(vv);
            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) { mp.setLooping(true); }
            });
        } else {
            Glide.with(_context)
                    .load("https://i.imgur.com/" + image.getId() + ".gif")
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(vh._image);
        }
        if (image.getDescription().equals("null")) {
            vh._text.setVisibility(View.GONE);
        } else {
            vh._text.setTextColor(_context.getColor(R.color.white));
            vh._text.setText(image.getDescription());
        }
    }

    @Override
    public int getItemCount() { return _album.size(); }
}

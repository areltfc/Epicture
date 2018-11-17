package eu.delattreepitech.arthur.dev_epicture_2018.Adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Types.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
import eu.delattreepitech.arthur.dev_epicture_2018.ViewHolders.AlbumImageViewHolder;

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
        AlbumImageViewHolder vh = new AlbumImageViewHolder(_context.getLayoutInflater().inflate(R.layout.album_image, null));
        vh._image = vh.itemView.findViewById(R.id.image);
        vh._video = vh.itemView.findViewById(R.id.video);
        vh._text = vh.itemView.findViewById(R.id.text);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        AlbumImageViewHolder vh = (AlbumImageViewHolder) viewHolder;
        final Image image = _album.get(i);
        if (image.getType() != null && image.getType().equals("mp4")) {
            vh._image.setVisibility(View.GONE);
            Uri uri = Uri.parse("https://i.imgur.com/" + image.getRealId() + ".mp4");
            MediaController controller = new MediaController(_context.getBaseContext());
            controller.setAnchorView(vh._video);
            vh._video.setVideoURI(uri);
            vh._video.setMediaController(controller);
            vh._video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) { mp.setLooping(true); }
            });
            vh._video.start();
        } else {
            vh._video.setVisibility(View.GONE);
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

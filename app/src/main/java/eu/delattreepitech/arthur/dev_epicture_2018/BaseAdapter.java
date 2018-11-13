package eu.delattreepitech.arthur.dev_epicture_2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import eu.delattreepitech.arthur.dev_epicture_2018.activities.Detailed;

public class BaseAdapter extends RecyclerView.Adapter {
    private AppCompatActivity _context;
    private List<Image> _images;
    private String _accessToken;

    public BaseAdapter(AppCompatActivity context, List<Image> images, String accessToken) {
        _context = context;
        _images = images;
        _accessToken = accessToken;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageViewHolder vh = new ImageViewHolder(_context.getLayoutInflater().inflate(R.layout.home_view_item, null));
        vh._image = vh.itemView.findViewById(R.id.photo);
        vh._name = vh.itemView.findViewById(R.id.title);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
        final String id = _images.get(i).getId();
        imageViewHolder._name.setText(_images.get(i).getName());
        Glide.with(_context)
                .load("https://i.imgur.com/" + _images.get(i).getId() + ".gif")
                .apply(new RequestOptions()
                        .fitCenter())
                .into(imageViewHolder._image);
        imageViewHolder._name.setText(_images.get(i).getName());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent detailed = new Intent(_context, Detailed.class);
                detailed.putExtra("id", id);
                detailed.putExtra("tokensUrl", _context.getIntent().getStringExtra("tokensUrl"));
                detailed.putExtra("accessToken", _accessToken);
                _context.startActivity(detailed);
            }
        };
        imageViewHolder._image.setOnClickListener(listener);
        imageViewHolder._name.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return _images.size();
    }
}

package eu.delattreepitech.arthur.dev_epicture_2018.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
import eu.delattreepitech.arthur.dev_epicture_2018.ViewHolders.TagViewHolder;

public class TagsAdapter extends RecyclerView.Adapter {
    private AppCompatActivity _context;
    private List<String> _tags;
    private User _user;

    public TagsAdapter(AppCompatActivity context, final List<String> tags, final User user) {
        _context = context;
        _tags = tags;
        _user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TagViewHolder vh = new TagViewHolder(_context.getLayoutInflater().inflate(R.layout.album_tag, null));
        vh._tag = vh.itemView.findViewById(R.id.text);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TagViewHolder vh = (TagViewHolder) viewHolder;
        final String tag = _tags.get(i);
        vh._tag.setTextColor(_context.getColor(R.color.white));
        vh._tag.setText(tag);
    }

    @Override
    public int getItemCount() { return _tags.size(); }
}
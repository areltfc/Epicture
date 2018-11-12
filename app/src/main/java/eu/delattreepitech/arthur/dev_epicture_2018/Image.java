package eu.delattreepitech.arthur.dev_epicture_2018;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Image {
    private String _id;
    private String _name;

    public Image(String id, String name, String url) {
        _id = id;
        _name = name;
    }

    public String getId() { return _id; }
    public String getName() { return _name; }

    public Drawable createDrawableFromId() throws IOException {
        InputStream is = (InputStream) new URL("https://i.imgur/" + _id + ".jpg").getContent();
        return Drawable.createFromStream(is, null);
    }

    public static Drawable createDrawableFromId(String id) throws IOException {
        InputStream is = (InputStream) new URL("https://i.imgur/" + id + ".jpg").getContent();
        return Drawable.createFromStream(is, null);
    }
}

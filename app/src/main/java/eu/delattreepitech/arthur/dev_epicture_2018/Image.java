package eu.delattreepitech.arthur.dev_epicture_2018;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Image {
    private String _id;
    private String _name;
    private String _type;
    private String _user;
    private List<String> _tags;

    public Image() { _tags = new ArrayList<>(); }
    public Image(JSONObject src) { _tags = new ArrayList<>(); this.fillFromJSON(src); }

    public String getId() { return _id; }
    public String getName() { return _name; }
    public String getType() { return _type; }
    public String getUser() { return _user; }
    public List<String> getTags() { return _tags; }

    public void fillFromJSON(final JSONObject src) {
        try {
            if (src.has("is_album") && src.getBoolean("is_album")) {
                _id = src.getString("cover");
            } else {
                _id = src.getString("id");
            }
            _name = src.getString("title");
            if (src.has("type")) {
                String type = src.getString("type");
                _type = type.substring(type.lastIndexOf("/") + 1);
            } else {
                _type = "jpeg";
            }
            _user = src.getString("account_url");
            if (src.has("tags")) {
                JSONArray tags = src.getJSONArray("tags");
                for (int i = 0; i < tags.length(); i++) {
                    _tags.add(tags.getJSONObject(i).getString("display_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.purify();
    }

    static public List<Image> createListFromJSON(final JSONObject src) throws JSONException {
        List<Image> list = new ArrayList<>();
        if (src.has("is_album") && src.getBoolean("is_album")) {
            JSONArray holder = new JSONArray(src.getJSONArray("images"));
            for (int i  = 0; i < holder.length(); i++) {
                list.add(new Image(holder.getJSONObject(i)));
            }
        } else {
            list.add(new Image(src));
        }
        return list;
    }

    private void purify() {
        if (_name.equals("null")) {
            _name = "(Untitled picture)";
        } else if (_name.length() > 40) {
            _name = _name.substring(0, 37) + "...";
        }
    }
}

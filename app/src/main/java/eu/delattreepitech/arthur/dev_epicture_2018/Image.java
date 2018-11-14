package eu.delattreepitech.arthur.dev_epicture_2018;

import org.json.JSONObject;

public class Image {
    private String _id;
    private String _name;
    private String _type;

    public Image() {}
    public Image(JSONObject src) { this.fillFromJSON(src); }

    public String getId() { return _id; }
    public String getName() { return _name; }
    public String getType() { return _type; }

    public void setId(final String id) { _id = id; }
    public void setName(final String name) { _name = name; }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.purify();
    }

    private void purify() {
        if (_name.equals("null")) {
            _name = "(Untitled picture)";
        } else if (_name.length() > 40) {
            _name = _name.substring(0, 37) + "...";
        }
    }
}

package eu.delattreepitech.arthur.dev_epicture_2018;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToList {
    public static List<Image> Images(String requestBody) throws JSONException {
        JSONObject obj = new JSONObject(requestBody);
        JSONArray data = obj.getJSONArray("data");
        final List<Image> images = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            Image image = new Image();
            image.fillFromJSON(item);
            images.add(image);
        }
        return images;
    }
}

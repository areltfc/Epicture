package eu.delattreepitech.arthur.dev_epicture_2018;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InterpretAPIRequest {
    public static List<Image> JSONToImages(String requestBody) throws JSONException {
        JSONObject obj = new JSONObject(requestBody);
        JSONArray data = obj.getJSONArray("data");
        final List<Image> images = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            images.add(new Image(data.getJSONObject(i)));
        }
        return images;
    }

    public static Image JSONToImage(String requestBody) throws JSONException {
        return new Image(new JSONObject(requestBody).getJSONObject("data"));
    }
}

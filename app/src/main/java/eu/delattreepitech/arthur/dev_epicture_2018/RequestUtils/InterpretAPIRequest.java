package eu.delattreepitech.arthur.dev_epicture_2018.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eu.delattreepitech.arthur.dev_epicture_2018.Types.Image;

public class InterpretAPIRequest {
    public static List<Image> JSONToImages(String requestBody) throws JSONException {
        JSONObject obj = new JSONObject(requestBody);
        Object data = obj.get("data");
        JSONArray dataArray;
        if (data instanceof JSONArray) {
            dataArray = (JSONArray) data;
        } else {
            dataArray = ((JSONObject) data).getJSONArray("items");
        }
        final List<Image> images = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            images.add(new Image(dataArray.getJSONObject(i)));
        }
        return images;
    }

    public static Image JSONToImage(String requestBody) throws JSONException {
        return new Image(new JSONObject(requestBody).getJSONObject("data"));
    }

    public static List<Image> JSONToAlbum(String requestBody) throws JSONException {
        return Image.createListFromJSON(new JSONObject(requestBody).getJSONObject("data"));
    }
}

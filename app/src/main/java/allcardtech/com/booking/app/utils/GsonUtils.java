package allcardtech.com.booking.app.utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonUtils {
    public GsonUtils() {
    }

    public static String toJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T getObjFromJsonString(String jstr, Class<T> typeToken) {
        Gson gson = new Gson();
        return gson.fromJson(jstr, typeToken);
    }

    public static <T> List<T> getListFormJsonString(String jstr, Class<T> typeTokem) {
        JsonParser parser = new JsonParser();
        JsonArray jsonElements = parser.parse(jstr).getAsJsonArray();
        Gson gson = new Gson();
        List<T> results = new ArrayList();
        Iterator var6 = jsonElements.iterator();

        while(var6.hasNext()) {
            JsonElement element = (JsonElement)var6.next();
            T t = gson.fromJson(element, typeTokem);
            results.add(t);
        }

        return results;
    }

    public static <T> Map<String, T> getMapFormJsonString(String jstr) {
        Gson gson = new Gson();
        Type type = (new TypeToken<HashMap<String, T>>() {
        }).getType();
        Map<String, T> map = (Map)gson.fromJson(jstr, type);
        return map;
    }
}

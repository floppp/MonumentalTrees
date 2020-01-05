package es.floppp.monumentaltreesgva.extras;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import es.floppp.monumentaltreesgva.pojos.Tree;


public class Deserializer {
    private JsonObject mObject;

    public Tree jsonToTree(JsonElement element, String region) {
        this.mObject = element.getAsJsonObject();
        return new Tree(
                getSafeInt(element, "id"),
                region,
                this.mObject.get("municipio").toString(),
                getSafeInt(element, "noInventario"),
                this.mObject.get("especie").toString(),
                getSafeInt(element, "x"),
                getSafeInt(element, "y"),
                getSafeInt(element, "edad"),
                getSafeFloat(element, "altura"),
                getSafeFloat(element, "perímetro"),
                getSafeFloat(element, "diamCopa"),
                getSafeFloat(element, "entornoProtección")
        );
    }

    private float getSafeFloat(JsonElement el, String key) {
        float value = -1;
        try {
            value = Float.parseFloat(this.mObject.get(key).toString());
        } catch (Exception ex) {

        }

        return value;
    }

    private int getSafeInt(JsonElement el, String key) {
        int value = -1;
        try {
            value = Integer.parseInt(this.mObject.get(key).toString());
        } catch (Exception ex) {

        }

        return value;
    }
}

//public class Deserializer<T> implements JsonDeserializer<T> {
//    private String mKey;
//
//    public Deserializer(String key) {
//        this.mKey = key;
//    }
//
//    @Override
//    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
//            throws JsonParseException {
//        JsonElement content = je.getAsJsonObject().get(this.mKey);
//        Log.d("PRUEBAS", content.toString());
//        return new Gson().fromJson(content, type);
//
//    }
//}
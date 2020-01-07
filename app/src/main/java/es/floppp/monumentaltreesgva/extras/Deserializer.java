package es.floppp.monumentaltreesgva.extras;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import es.floppp.monumentaltreesgva.pojos.Tree;


public class Deserializer {
    private JsonObject mObject;

    public Tree jsonToTree(JsonElement element, String region) {
        this.mObject = element.getAsJsonObject();

        WGS84 wgs = new WGS84(new UTM(30, 'N', getSafeInt("x"), getSafeInt("y")));
        return new Tree(
                getSafeInt("id"),
                region,
                this.mObject.get("municipio").toString(),
                getSafeInt("noInventario"),
                this.mObject.get("especie").toString(),
                wgs.getLatitude(),
                wgs.getLongitude(),
                getSafeInt("edad"),
                getSafeFloat("altura"),
                getSafeFloat("perímetro"),
                getSafeFloat("diamCopa"),
                getSafeFloat("entornoProtección")
        );
    }

    private float getSafeFloat(String key) {
        float value = 0;
        try {
            value = Float.parseFloat(this.mObject.get(key).toString());
        } catch (Exception ignored) {

        }

        return value;
    }

    private int getSafeInt(String key) {
        int value = 0;
        try {
            value = Integer.parseInt(this.mObject.get(key).toString());
        } catch (Exception ignored) {

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
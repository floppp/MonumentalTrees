package es.floppp.monumentaltreesgva.pojos;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.io.Serializable;


@Entity(tableName = "tree",
        primaryKeys = {"id", "regionName"})
public class Tree implements Serializable {
    public final int id;
    @NonNull public final String regionName;
    public final String town;
    public final int inventary;
    public final String species;
    public final double lat;
    public final double lon;
    public final int age;
    public final float height;
    public final float perimeter;
    public final float diameter;
    public final float protection;

    public Tree(int id,
                String regionName,
                String town,
                int inventary,
                String species,
                double lat,
                double lon,
                int age,
                float height,
                float perimeter,
                float diameter,
                float protection) {
        this.id = id;
        this.regionName = regionName.replaceAll("\"", "");
        this.town = town.replaceAll("\"", "");
        this.inventary = inventary;
        this.species = species.replaceAll("\"", "");
        this.lat = lat;
        this.lon = lon;
        this.age = age;
        this.height = height;
        this.perimeter = perimeter;
        this.diameter = diameter;
        this.protection = protection;
    }
}

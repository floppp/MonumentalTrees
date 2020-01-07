package es.floppp.monumentaltreesgva.pojos;

import androidx.annotation.NonNull;
import androidx.room.Entity;


@Entity(tableName = "tree",
        primaryKeys = {"id", "regionName"})
public class Tree {
    public final int id;
    @NonNull public final String regionName;
    public final String town;
    public final int inventary;
    public final String species;
//    public final int x;
//    public final int y;
    public final double lat;
    public final double lon;
    public final int age;
    public final float height;
    public final float perimeter;
    public final float diameter;
//    public final String property;
    public final float protection;

    public Tree(int id,
                String regionName,
                String town,
                int inventary,
                String species,
//                int x,
//                int y,
                double lat,
                double lon,
                int age,
                float height,
                float perimeter,
                float diameter,
                float protection) {
        this.id = id;
        this.regionName = regionName;
        this.town = town;
        this.inventary = inventary;
        this.species = species;
        this.lat = lat;
        this.lon = lon;
        this.age = age;
        this.height = height;
        this.perimeter = perimeter;
        this.diameter = diameter;
        this.protection = protection;
    }
}

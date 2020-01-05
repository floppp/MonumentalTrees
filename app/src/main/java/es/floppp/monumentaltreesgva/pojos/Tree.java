package es.floppp.monumentaltreesgva.pojos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "tree",
        primaryKeys = {"id", "regionName"})
public class Tree {
    public final int id;
    @NonNull public final String regionName;
    public final String town;
    public final int inventary;
    public final String species;
    public final int x;
    public final int y;
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
                int x,
                int y,
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
        this.x = x;
        this.y = y;
        this.age = age;
        this.height = height;
        this.perimeter = perimeter;
        this.diameter = diameter;
        this.protection = protection;
    }
}

package es.floppp.monumentaltreesgva.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import es.floppp.monumentaltreesgva.pojos.Tree;


@Dao
public interface TreeDao {
    @Query("SELECT * from tree")
    LiveData<List<Tree>> getAllTrees();

    @Query("SELECT * from tree WHERE regionName=:region")
    LiveData<List<Tree>> getRegionTrees(String region);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tree tree);

    @Query("DELETE FROM tree")
    void deleteAllTrees();
}

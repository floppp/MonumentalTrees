package es.floppp.monumentaltreesgva.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.floppp.monumentaltreesgva.pojos.Tree;


@Database(entities = {Tree.class}, version = 1, exportSchema = false)
public abstract class TreeDatabase extends RoomDatabase {
    public abstract TreeDao treeDao();

    private static volatile TreeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // TODO: Para pruebas, luego borrar
//            dbWriteExecutor.execute(() -> INSTANCE.treeDao().deleteAllTrees());
        }
    };

    // Pool con los hilos disponibles para ejecutar acciones sobre/contra la base de datos.
    public static final ExecutorService dbWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TreeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TreeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TreeDatabase.class,
                            "tree_database"
                    ).addCallback(sRoomDatabaseCallback)
                     .build();
                }
            }
        }

        return INSTANCE;
    }
}

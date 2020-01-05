package es.floppp.monumentaltreesgva.usecases;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.floppp.monumentaltreesgva.extras.CustomCallback;
import es.floppp.monumentaltreesgva.extras.K;
import es.floppp.monumentaltreesgva.models.HttpRequest;
import es.floppp.monumentaltreesgva.models.TreeDao;
import es.floppp.monumentaltreesgva.models.TreeDatabase;
import es.floppp.monumentaltreesgva.pojos.Tree;


public class TreesRepository {
    private HttpRequest mWebService;
    private TreeDao mTreeDao;

    public TreesRepository(TreeDao dao, HttpRequest webService) {
        this.mTreeDao = dao;
        this.mWebService = webService;
    }

    public LiveData<List<Tree>> getAllTrees() {
        this.checkDbCache();
        Log.d("REPOSITORY", "llamamos a getAllTrees");
        return this.mTreeDao.getAllTrees();
    }

    public void insert(Tree word) {
        TreeDatabase.executorService.execute(() -> this.mTreeDao.insert(word));
    }

    private void checkDbCache() {
        TreeDatabase.executorService.execute(() -> {
            if (this.mTreeDao.getNumberOfEntries() == 0) {
                Log.d("REPOSITORY", "Hacemos nueva petición");
                this.mWebService.makeTreesRequest(K.Region.VALENCIA);
            } else {
                Log.d("REPOSITORY", "Obtenemos árboles de base de datos");
            }
        });
    }
}

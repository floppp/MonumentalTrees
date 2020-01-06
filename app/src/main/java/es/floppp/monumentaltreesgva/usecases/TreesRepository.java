package es.floppp.monumentaltreesgva.usecases;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

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
        return this.mTreeDao.getAllTrees();
    }

    public LiveData<List<Tree>> getRegionTrees(K.Region region) {
        this.checkDbCache(region);
        return this.mTreeDao.getRegionTrees(K.END_POINTS.get(region));
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

    private void checkDbCache(K.Region region) {
        TreeDatabase.executorService.execute(() -> {
            if (this.mTreeDao.getNumberOfEntriesInRegion(K.END_POINTS.get(region)) == 0) {
                Log.d("REPOSITORY", "Hacemos nueva petición para " + region);
                this.mWebService.makeTreesRequest(region);
            } else {
                Log.d("REPOSITORY", "Obtenemos árboles de base de datos de " + region);
            }
        });
    }
}

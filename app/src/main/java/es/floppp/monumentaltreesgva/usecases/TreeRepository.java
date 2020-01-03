package es.floppp.monumentaltreesgva.usecases;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.floppp.monumentaltreesgva.models.TreeDao;
import es.floppp.monumentaltreesgva.models.TreeDatabase;
import es.floppp.monumentaltreesgva.pojos.Tree;


public class TreeRepository {
    private TreeDao mTreeDao;
    private LiveData<List<Tree>> mTrees;

    public TreeRepository(TreeDao dao) {
//        TreeDatabase db = TreeDatabase.getDatabase(application);
        this.mTreeDao = dao;
        mTrees = this.mTreeDao.getAllTrees();
    }

    public LiveData<List<Tree>> getAllTrees() {
        return mTrees;
    }

    public void insert(Tree word) {
        TreeDatabase.dbWriteExecutor.execute(() -> this.mTreeDao.insert(word));
    }
}

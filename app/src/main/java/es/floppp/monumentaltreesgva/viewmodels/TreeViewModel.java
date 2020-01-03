package es.floppp.monumentaltreesgva.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.floppp.monumentaltreesgva.models.TreeDatabase;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.usecases.TreeRepository;


public class TreeViewModel extends AndroidViewModel {
    private TreeRepository mRepository;
    private LiveData<List<Tree>> mTrees;

    public TreeViewModel(@NonNull Application application) {
        super(application);
        TreeDatabase db = TreeDatabase.getDatabase(application);
        mRepository = new TreeRepository(db.treeDao());
        mTrees = mRepository.getAllTrees();
    }

    public LiveData<List<Tree>> getAllTrees() {
        return mTrees;
    }

    public void insert(Tree tree) {
        mRepository.insert(tree);
    }
}

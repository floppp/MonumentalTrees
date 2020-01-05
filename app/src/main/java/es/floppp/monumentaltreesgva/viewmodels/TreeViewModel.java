package es.floppp.monumentaltreesgva.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.floppp.monumentaltreesgva.MainActivity;
import es.floppp.monumentaltreesgva.extras.CustomCallback;
import es.floppp.monumentaltreesgva.models.HttpRequest;
import es.floppp.monumentaltreesgva.models.TreeDatabase;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.usecases.TreesRepository;


public class TreeViewModel extends AndroidViewModel {
    private TreesRepository mRepository;
    private LiveData<List<Tree>> mTrees;

    public TreeViewModel(@NonNull Application application) {
        super(application);
        TreeDatabase db = TreeDatabase.getDatabase(application);

        CustomCallback<Tree> callback = trees -> trees.forEach(tree ->
                new Thread(() -> TreeDatabase
                        .getDatabase(application)
                        .treeDao()
                        .insert(tree)
                ).start());

        mRepository = new TreesRepository(
                db.treeDao(),
                new HttpRequest(callback)
        );

        mTrees = mRepository.getAllTrees();
    }

    public LiveData<List<Tree>> getAllTrees() {
        return mTrees;
    }
}

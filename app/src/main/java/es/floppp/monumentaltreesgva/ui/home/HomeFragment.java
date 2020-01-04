package es.floppp.monumentaltreesgva.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.FragmentHomeBinding;
import es.floppp.monumentaltreesgva.extras.CustomCallback;
import es.floppp.monumentaltreesgva.extras.K;
import es.floppp.monumentaltreesgva.models.HttpRequest;
import es.floppp.monumentaltreesgva.models.TreeDatabase;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.viewmodels.TreeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TreeViewModel mTreeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentHomeBinding.inflate(inflater);

//        HttpRequest controller = new HttpRequest(this);
//        controller.makeTreesRequest(K.Region.VALENCIA);

        TreesAdapter adapter = new TreesAdapter(this.getContext());
        this.binding.rcvTrees.setAdapter(adapter);
        this.binding.rcvTrees.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mTreeViewModel = new ViewModelProvider(this).get(TreeViewModel.class);
        mTreeViewModel.getAllTrees().observe(getViewLifecycleOwner(), trees -> {
            adapter.setTrees(trees);
            adapter.notifyDataSetChanged();
        });

        return this.binding.getRoot();
    }

//    @Override
//    public void callback(List<Tree> trees) {
////        Log.d("PRUEBAS", "Recibidos " + trees.size() + " árboles.");
//        trees.forEach(tree -> new Thread(() -> TreeDatabase.getDatabase(getActivity().getApplication()).treeDao().insert(tree)).start());
//
//    }
}
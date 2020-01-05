package es.floppp.monumentaltreesgva.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import es.floppp.monumentaltreesgva.databinding.FragmentHomeBinding;
import es.floppp.monumentaltreesgva.viewmodels.TreeViewModel;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater);

        TreesAdapter adapter = new TreesAdapter(this.getContext());
        binding.rcvTrees.setAdapter(adapter);
        binding.rcvTrees.setLayoutManager(new LinearLayoutManager(this.getContext()));

        TreeViewModel mTreeViewModel = new ViewModelProvider(this).get(TreeViewModel.class);
        mTreeViewModel.getAllTrees().observe(
                getViewLifecycleOwner(),
                trees -> {
                    Log.d("HOME_FRAGMENT", "Recibimos " + trees.size() + " árboles");
                    adapter.setTrees(trees);
                    adapter.notifyDataSetChanged();
                });

        return binding.getRoot();
    }
}
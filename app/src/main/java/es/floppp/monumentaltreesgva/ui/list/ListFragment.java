package es.floppp.monumentaltreesgva.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.Objects;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.FragmentListBinding;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.viewmodels.TreesViewModel;
import es.floppp.monumentaltreesgva.viewmodels.UserSelectionsViewModel;

public class ListFragment extends Fragment {
    FragmentListBinding binding;
    TreesViewModel mTreeViewModel;
    UserSelectionsViewModel mUserSelectionViewModel;
    LiveData<List<Tree>> mLastLiveData;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentListBinding.inflate(inflater);

        this.mTreeViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(TreesViewModel.class);
        this.mUserSelectionViewModel = new ViewModelProvider(getActivity()).get(UserSelectionsViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TreesAdapter adapter = new TreesAdapter(tree -> {
            this.mUserSelectionViewModel.postTree(tree);
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.navigation_details);
        });

        this.binding.rcvTrees.setAdapter(adapter);
        this.binding.rcvTrees.setLayoutManager(new LinearLayoutManager(this.getContext()));


        mUserSelectionViewModel.regionChanged()
                .observe(
                        getViewLifecycleOwner(),
                        region -> {
                            Log.d("HOME_FRAGMENT", "Región recibida es " + region.toString());
                            ListFragment.this.binding.listProgressCircular.setVisibility(View.VISIBLE);

                            if (this.mLastLiveData != null) {
                                this.mLastLiveData.removeObservers(getViewLifecycleOwner());
                            }

                            this.mLastLiveData = mTreeViewModel.getRegionTrees(region);
                            this.mLastLiveData.observe(
                                    getViewLifecycleOwner(),
                                    trees -> {
                                        Log.d("HOME_FRAGMENT", "Recibimos " + trees.size() + " árboles");
                                        adapter.setTrees(trees);
                                        adapter.notifyDataSetChanged();
                                        if (trees.size() > 0) {
                                            ListFragment.this.binding.listProgressCircular.setVisibility(View.GONE);
                                        }
                                    });
                    }
            );
    }
}

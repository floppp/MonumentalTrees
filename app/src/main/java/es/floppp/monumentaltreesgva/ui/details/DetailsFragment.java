package es.floppp.monumentaltreesgva.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.FragmentDetailsBinding;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.viewmodels.UserSelectionsViewModel;


public class DetailsFragment extends Fragment {
    private UserSelectionsViewModel mUserSelectionViewModel;
    private FragmentDetailsBinding binding;
    private Tree mTree;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentDetailsBinding.inflate(inflater);
        this.mUserSelectionViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(UserSelectionsViewModel.class);

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Usar navigation graph para pasar información, en vez de livedata.
        this.mUserSelectionViewModel
                .treeSelected()
                .observe(getViewLifecycleOwner(), this::setInfoViews);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            this.mTree = (Tree) savedInstanceState.getSerializable("tree");
            setInfoViews(this.mTree);
        }

        this.binding.infoButton.setOnClickListener(view -> {
            String baseUrl = "https://en.wikipedia.org/wiki/";
            String[] fields = this.mTree.species.split(" ", 2);
            StringBuilder query = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                query.append(fields[i]);
                if (i + 1 < fields.length)
                    query.append("_");
            }
            // Poniendo dos campos pocas veces obtenemos resultado
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + fields[0]));
            startActivity(browserIntent);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putSerializable("tree", this.mTree);
    }

    private void setInfoViews(Tree tree) {
        this.mTree = tree;
        this.binding.txvSpecies.setText(tree.species);
        this.binding.txvTown.setText(tree.town + " (" + tree.regionName + ")");
        this.binding.txvHeight.setText(tree.height > 0 ?
                tree.height + " m" :
                getResources().getString(R.string.unkown_suffix));
        this.binding.txvDiameter.setText(tree.height > 0.1 ?
                tree.diameter + " m" :
                getResources().getString(R.string.unkown_suffix));
        this.binding.txvPerimeter.setText(tree.perimeter > 0.1 ?
                tree.diameter + " m" :
                getResources().getString(R.string.unkown_suffix));
        this.binding.txvAge.setText(tree.age > 0 ?
                tree.age + " " + getResources().getString(R.string.years)  :
                getResources().getString(R.string.age_preffix) + " " + getResources().getString(R.string.unkown_suffix));
    }
}

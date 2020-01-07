package es.floppp.monumentaltreesgva.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import es.floppp.monumentaltreesgva.databinding.FragmentDetailsBinding;
import es.floppp.monumentaltreesgva.viewmodels.UserSelectionsViewModel;


public class DetailsFragment extends Fragment {
    private UserSelectionsViewModel mUserSelectionViewModel;
    private FragmentDetailsBinding binding;

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
                .observe(
                        getViewLifecycleOwner(),
                        tree -> {
                            this.binding.txvSpecies.setText(tree.species);
                            this.binding.txvTown.setText(tree.town);
                        });
    }
}

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import es.floppp.monumentaltreesgva.databinding.FragmentListBinding;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.viewmodels.RegionViewModel;
import es.floppp.monumentaltreesgva.viewmodels.TreeViewModel;

public class ListFragment extends Fragment {
    FragmentListBinding binding;
    TreeViewModel mTreeViewModel;
    LiveData<List<Tree>> mLastLiveData;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentListBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Esto es lo que me da problemas a la hora de intentar compartir el view model entre
        // activity y los fragment hijos.
//        NavHostFragment childHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        NavController childNavController = childHostFragment.getNavController();
//        TreeViewModel treeViewModel =
//                new ViewModelProvider(childNavController.getViewModelStoreOwner(R.navigation.mobile_navigation)).get(TreeViewModel.class);

        TreesAdapter adapter = new TreesAdapter();
        this.binding.rcvTrees.setAdapter(adapter);
        this.binding.rcvTrees.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mTreeViewModel = new ViewModelProvider(getActivity()).get(TreeViewModel.class);

        new ViewModelProvider(getActivity())
                .get(RegionViewModel.class)
                .regionChanged()
                .observe(
                        getViewLifecycleOwner(),
                        region -> {
                            Log.d("HOME_FRAGMENT", "Región recibida es " + region.toString());
                            ListFragment.this.binding.listProgressCircular.setVisibility(View.VISIBLE);

                            // Eliminarmos observers y así evitamos que la primera petición chafe a la segunda,
                            // cosa que ocurre cuando el repository usa red al no haber cacheado en sqllite.
                            if (this.mLastLiveData != null) {
                                this.mLastLiveData.removeObservers(getViewLifecycleOwner());
                            }

                            // TODO: con cada cambio hacemos una subscipción, habría que cambiarlo.
                            //  He hecho un profiling y la memoria no aumenta, a pesar de cambiar más de 20 veinte
                            //  veces de provincia. Pero no me gusta mucho, así que creo hay que cambiarlo.
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

package es.floppp.monumentaltreesgva.ui.map;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.FragmentMapBinding;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.viewmodels.TreesViewModel;
import es.floppp.monumentaltreesgva.viewmodels.UserSelectionsViewModel;

public class MapFragment extends Fragment {

    private Handler mHandler = new Handler();
    private FragmentMapBinding binding;
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 1;
    private SupportMapFragment mapFragment;
    TreesViewModel mTreeViewModel;
    UserSelectionsViewModel mUserSelectionViewModel;
    LiveData<List<Tree>> mLastLiveData;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentMapBinding.inflate(inflater);

        this.mTreeViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(TreesViewModel.class);
        this.mUserSelectionViewModel = new ViewModelProvider(getActivity()).get(UserSelectionsViewModel.class);

        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mUserSelectionViewModel.regionChanged()
                            .observe(
                                    getViewLifecycleOwner(),
                                    region -> {
                                        if (MapFragment.this.mLastLiveData != null) {
                                            MapFragment.this.mLastLiveData.removeObservers(getViewLifecycleOwner());
                                        }

                                        MapFragment.this.mLastLiveData = mTreeViewModel.getRegionTrees(region);
                                        MapFragment.this.mLastLiveData.observe(
                                                getViewLifecycleOwner(),
                                                trees -> {
                                                    setZoomAndCenter(googleMap, trees);
                                                    createTreeMarkers(googleMap, trees);
                                                });
                                    }
                            );


                }
            });
        }

        return binding.getRoot();
    }

    private void setZoomAndCenter(GoogleMap googleMap, List<Tree> trees) {
        new Thread(() -> {
            double len = trees.parallelStream().filter(tree -> tree.lat != 0 && tree.lon != 0).count();
            // Esto podría hacerse de una pasada con un reduce y un diccionario o un array como acumulador.
            double lat = trees.parallelStream().filter(tree -> tree.lat != 0 && tree.lon != 0).mapToDouble(t -> t.lat).sum() / len;
            double lon = trees.parallelStream().filter(tree -> tree.lat != 0 && tree.lon != 0).mapToDouble(t -> t.lon).sum() / len;

            mHandler.post(() -> googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 8f)));
        }).start();
    }

    private void createTreeMarkers(GoogleMap googleMap, List<Tree> trees) {
        googleMap.clear();
        trees.stream()
             .filter(tree -> tree.lat != 0 && tree.lon != 0)
             .forEach(tree ->
                     googleMap.addMarker(new MarkerOptions().title(tree.species).position(new LatLng(tree.lat, tree.lon)))
                );
    }
    private void checkMapPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Actualmente podemos conocer su ubicación.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Vamos a solicitar acceder a su localización.", Toast.LENGTH_LONG).show();

            permissionRequets(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    "Permiso necesario para poder situarnos en el mapa.",
                    MapFragment.FINE_LOCATION_PERMISSION_REQUEST
            );
        }
    }

    private void permissionRequets(String permission, String justification, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Solicitud de permiso");
            alertDialogBuilder.setMessage(justification);
            alertDialogBuilder.setPositiveButton(
                    "Ok",
                    (dialog, id) -> ActivityCompat.requestPermissions(getActivity(), new String[]{ permission }, requestCode));
            alertDialogBuilder.setNegativeButton(
                    "Cancelar",
                    (dialog, id) -> Toast.makeText(getContext(), "permiso no concedido", Toast.LENGTH_LONG).show());
            alertDialogBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ permission }, requestCode);
        }
    }
}
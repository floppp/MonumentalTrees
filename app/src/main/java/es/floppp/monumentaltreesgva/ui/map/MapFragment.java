package es.floppp.monumentaltreesgva.ui.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.FragmentMapBinding;
import es.floppp.monumentaltreesgva.pojos.Tree;
import es.floppp.monumentaltreesgva.viewmodels.TreesViewModel;
import es.floppp.monumentaltreesgva.viewmodels.UserSelectionsViewModel;

public class MapFragment extends Fragment {

    private static final int FINE_LOCATION_PERMISSION_REQUEST = 1;
    private Handler mHandler = new Handler();
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private TreesViewModel mTreeViewModel;
    private UserSelectionsViewModel mUserSelectionViewModel;
    private LiveData<List<Tree>> mLastLiveData;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        es.floppp.monumentaltreesgva.databinding.FragmentMapBinding binding = FragmentMapBinding.inflate(inflater);

        this.mTreeViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(TreesViewModel.class);
        this.mUserSelectionViewModel = new ViewModelProvider(getActivity()).get(UserSelectionsViewModel.class);

        this.checkMapPermission();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(googleMap -> {
                googleMap.setOnMarkerClickListener(marker -> {
                    Navigation.findNavController(Objects.requireNonNull(MapFragment.this.getView())).navigate(R.id.navigation_details);
                    MapFragment.this.mUserSelectionViewModel.postTree((Tree) marker.getTag());

                    return true;
                });
                this.mGoogleMap = googleMap;
                this.setMap();
                this.setObservers();
            });
        } else if (this.mGoogleMap != null) {
            // Rama para lidiar con la destrucción del livedata asociado al viewmodel al 'navigateUp'.
            this.setObservers();
        }
    }

    private void setObservers() {
        this.mUserSelectionViewModel
                .regionChanged()
                .observe(getViewLifecycleOwner(),
                        region -> {

                            if (MapFragment.this.mLastLiveData != null) {
                                MapFragment.this.mLastLiveData.removeObservers(getViewLifecycleOwner());
                            }

                            MapFragment.this.mLastLiveData = mTreeViewModel.getRegionTrees(region);
                            MapFragment.this.mLastLiveData.observe(
                                    getViewLifecycleOwner(),
                                    trees -> {
                                        setZoomAndCenter(this.mGoogleMap, trees);
                                        createTreeMarkers(this.mGoogleMap, trees);
                                    });
                        }
                );
    }

    private void setZoomAndCenter(GoogleMap googleMap, List<Tree> trees) {
        new Thread(() -> {
            double len = trees.parallelStream().filter(tree -> tree.lat != 0 && tree.lon != 0).count();
            // Esto podría hacerse de una pasada con un reduce y un diccionario o un array como acumulador.
            double lat = trees.parallelStream().filter(tree -> tree.lat != 0 && tree.lon != 0).mapToDouble(t -> t.lat).sum() / len;
            double lon = trees.parallelStream().filter(tree -> tree.lat != 0 && tree.lon != 0).mapToDouble(t -> t.lon).sum() / len;

            mHandler.post(() -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 8f)));
        }).start();
    }

    private void createTreeMarkers(GoogleMap googleMap, List<Tree> trees) {
        googleMap.clear();
        trees.stream()
             .filter(tree -> tree.lat != 0 && tree.lon != 0)
             .forEach(tree -> {
                 Marker marker = googleMap.addMarker(
                         new MarkerOptions()
                                 .title(tree.species)
                                 .position(new LatLng(tree.lat, tree.lon))
                                 .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.custom_marker))
                 );
                 marker.setTag(tree);
             });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(getActivity(), "Vamos a preguntar.", Toast.LENGTH_SHORT).show();

        if (requestCode == MapFragment.FINE_LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permisos de localización concedidos correctamente.", Toast.LENGTH_SHORT).show();
                setMap();
            } else {
                Toast.makeText(getActivity(), "Sin el permiso, no podemos saber su ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkMapPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getContext(), "Actualmente podemos conocer su ubicación.", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(getContext(), "Vamos a solicitar acceder a su localización.", Toast.LENGTH_LONG).show();

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

    private void setMap() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }
}
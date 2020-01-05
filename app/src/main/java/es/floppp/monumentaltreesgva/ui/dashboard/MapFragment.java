package es.floppp.monumentaltreesgva.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import es.floppp.monumentaltreesgva.R;
import es.floppp.monumentaltreesgva.databinding.FragmentDashboardBinding;

public class MapFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 1;
    private SupportMapFragment mapFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentDashboardBinding.inflate(inflater);
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng latLng = new LatLng(1.289545, 103.849972);
                    googleMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Singapore"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });
        }

        return binding.getRoot();
    }

    private void checkMapPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
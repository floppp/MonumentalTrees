package es.floppp.monumentaltreesgva;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.floppp.monumentaltreesgva.databinding.ActivityMainBinding;
import es.floppp.monumentaltreesgva.viewmodels.RegionViewModel;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RegionViewModel mRegionVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        setSupportActionBar(this.binding.toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
        ).build();

        this.mRegionVm = new ViewModelProvider(this).get(RegionViewModel.class);
        this.mRegionVm.post(0); // iniciamos con Valencia.

        this.setUpNavigation(appBarConfiguration);
        this.spinnerSetUp();
    }


    private void setUpNavigation(AppBarConfiguration appBarConfiguration) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(this.binding.navView, navController);

        // He intentado compartir el view model entre fragments a partir del grafo
        // de navegación, no lo he conseguido, he sido incapaz de recuperar de los
        // fragments (el nav_host_fragment) en ellos.
//        this.mTreeVM = new ViewModelProvider(navController.getViewModelStoreOwner(R.id.mobile_navigation)).get(TreeViewModel.class);
    }

    private void spinnerSetUp() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.regions,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.spinner.setAdapter(adapter);
        this.binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.mRegionVm.post(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

}

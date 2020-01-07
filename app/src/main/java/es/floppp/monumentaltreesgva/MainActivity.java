package es.floppp.monumentaltreesgva;

import android.os.Bundle;
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
import es.floppp.monumentaltreesgva.extras.K;
import es.floppp.monumentaltreesgva.viewmodels.UserSelectionsViewModel;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    UserSelectionsViewModel mRegionVm;
    NavController mNavController;
    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        setSupportActionBar(this.binding.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
        ).build();

        this.mRegionVm = new ViewModelProvider(this).get(UserSelectionsViewModel.class);
        this.mRegionVm.postRegion(K.Region.VALENCIA.ordinal());

        this.setUpNavigation(mAppBarConfiguration);
        this.spinnerSetUp();
    }


    @Override
    public boolean onSupportNavigateUp() {
        this.mNavController.navigateUp();

        return true;
    }

    private void setUpNavigation(AppBarConfiguration appBarConfiguration) {
        this.mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(this.binding.navView, mNavController);

        this.mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id != R.id.navigation_dashboard && id != R.id.navigation_home && id != R.id.navigation_notifications) {
                this.binding.navView.setVisibility(View.GONE);
                this.binding.spinner.setVisibility(View.GONE);
            } else {
                this.binding.navView.setVisibility(View.VISIBLE);
                this.binding.spinner.setVisibility(View.VISIBLE);
            }
        });
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
                MainActivity.this.mRegionVm.postRegion(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

}

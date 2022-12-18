package me.rubik.rubikscube;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import org.opencv.android.OpenCVLoader;

import me.rubik.rubikscube.database.DatabaseHandler;
import me.rubik.rubikscube.databinding.ActivityMainBinding;
import me.rubik.rubikscube.solver.Search;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Search.init();
        OpenCVLoader.initDebug();
        DatabaseHandler.init(Room.databaseBuilder(getApplicationContext(), DatabaseHandler.class, "database-name").build());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_solver, R.id.navigation_timer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == ) {
//            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Camera permission required to insert cube with camera.", Toast.LENGTH_LONG).show();
//                this.finish();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }


}
package me.rubik.rubikscube.ui.helpScreen;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import me.rubik.rubikscube.databinding.ActivityHelpScreenBinding;


public class HelpScreenActivity extends AppCompatActivity {

    private ActivityHelpScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHelpScreenBinding.inflate(getLayoutInflater());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Help");
        setContentView(binding.getRoot());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
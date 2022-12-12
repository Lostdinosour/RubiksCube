package me.rubic.rubikscube;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import me.rubic.rubikscube.databinding.ActivityInsertCubeBinding;
import me.rubic.rubikscube.databinding.ActivityInsertSideBinding;
import me.rubic.rubikscube.utils.CubeEnum;

public class InsertSideActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private ActivityInsertSideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertSideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        int color = intent.getIntExtra("color", ContextCompat.getColor(this.getBaseContext(), R.color.black));
        binding.buttonTopLeft.setBackgroundColor(color);
        binding.buttonTopCenter.setBackgroundColor(color);
        binding.buttonTopRight.setBackgroundColor(color);
        binding.buttonLeftCenter.setBackgroundColor(color);
        binding.buttonCenter.setBackgroundColor(color);
        binding.buttonRightCenter.setBackgroundColor(color);
        binding.buttonBottomLeft.setBackgroundColor(color);
        binding.buttonBottomCenter.setBackgroundColor(color);
        binding.buttonBottomRight.setBackgroundColor(color);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert cube");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ClickListener implements View.OnClickListener {

        private final CubeEnum square;

        public ClickListener(CubeEnum square) {
            this.square = square;
        }

        @Override
        public void onClick(View view) {
            switch (square) {
                case topLeft:
            }
        }
    }
}
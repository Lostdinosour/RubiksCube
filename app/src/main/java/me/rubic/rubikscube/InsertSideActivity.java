package me.rubic.rubikscube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import me.rubic.rubikscube.databinding.ActivityInsertCubeBinding;
import me.rubic.rubikscube.databinding.ActivityInsertSideBinding;

public class InsertSideActivity extends AppCompatActivity {

    private ActivityInsertSideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertSideBinding.inflate(getLayoutInflater());
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
        setContentView(binding.getRoot());
    }
}
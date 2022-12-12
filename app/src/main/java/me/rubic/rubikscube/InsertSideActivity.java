package me.rubic.rubikscube;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

        binding.buttonTopLeft.setOnClickListener(new ClickListener(CubeEnum.topLeft));
        binding.buttonTopCenter.setOnClickListener(new ClickListener(CubeEnum.topCenter));
        binding.buttonTopRight.setOnClickListener(new ClickListener(CubeEnum.topRight));
        binding.buttonLeftCenter.setOnClickListener(new ClickListener(CubeEnum.leftCenter));
        binding.buttonCenter.setOnClickListener(new ClickListener(CubeEnum.center));
        binding.buttonRightCenter.setOnClickListener(new ClickListener(CubeEnum.rightCenter));
        binding.buttonBottomLeft.setOnClickListener(new ClickListener(CubeEnum.bottomLeft));
        binding.buttonBottomCenter.setOnClickListener(new ClickListener(CubeEnum.bottomCenter));
        binding.buttonBottomRight.setOnClickListener(new ClickListener(CubeEnum.bottomRight));

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

    private class ClickListener implements View.OnClickListener {

        private final CubeEnum square;

        public ClickListener(CubeEnum square) {
            this.square = square;
        }

        @Override
        public void onClick(View view) {
            int currentColor = ContextCompat.getColor(InsertSideActivity.this, R.color.black);
            int color = ContextCompat.getColor(InsertSideActivity.this, R.color.black);

            Button button = null;
            switch (square) {
                case topLeft:
                    button = binding.buttonTopLeft;
                    break;
                case topCenter:
                    button = binding.buttonTopCenter;
                    break;
                case topRight:
                    button = binding.buttonTopRight;
                    break;
                case leftCenter:
                    button = binding.buttonLeftCenter;
                    break;
                case center:
                    button = binding.buttonCenter;
                    break;
                case rightCenter:
                    button = binding.buttonRightCenter;
                    break;
                case bottomLeft:
                    button = binding.buttonBottomLeft;
                    break;
                case bottomCenter:
                    button = binding.buttonBottomCenter;
                    break;

                case bottomRight:
                    button = binding.buttonBottomRight;
                    break;

            }
            if (button != null) {
                currentColor = ((ColorDrawable) button.getBackground()).getColor();
                color = getNextColor(currentColor);
                button.setBackgroundColor(color);
            }
        }

        private int getNextColor(int currentColor) {
            switch (currentColor) {
                case R.color.black:
                    return R.color.black;
                case R.color.red:
                    return R.color.green;
                case R.color.green:
                    return R.color.white;
                case R.color.white:
                    return R.color.blue;
                case R.color.blue:
                    return R.color.orange;
                case R.color.orange:
                    return R.color.yellow;
                case R.color.yellow:
                    return R.color.red;
            }

            return R.color.black;
        }
    }
}
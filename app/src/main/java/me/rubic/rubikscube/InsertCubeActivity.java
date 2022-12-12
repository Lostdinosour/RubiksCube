package me.rubic.rubikscube;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import me.rubic.rubikscube.databinding.ActivityInsertCubeBinding;
import me.rubic.rubikscube.databinding.ActivityMainBinding;
import me.rubic.rubikscube.utils.CubeEnum;

public class InsertCubeActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private ActivityInsertCubeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertCubeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonCubeTop.setOnClickListener(new ClickListener(CubeEnum.sideTop));
        binding.buttonCubeLeft.setOnClickListener(new ClickListener(CubeEnum.sideLeft));
        binding.buttonCubeFront.setOnClickListener(new ClickListener(CubeEnum.sideFront));
        binding.buttonCubeRight.setOnClickListener(new ClickListener(CubeEnum.sideRight));
        binding.buttonCubeBottom.setOnClickListener(new ClickListener(CubeEnum.sideBottom));
        binding.buttonCubeBack.setOnClickListener(new ClickListener(CubeEnum.sideBack));
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert cube");

//        binding.buttonCubeTop.setOnClickListener(new ClickListener(CubeEnum.sideTop));

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
        private final CubeEnum side;

        public ClickListener(CubeEnum side) {
            this.side = side;
        }

        @Override
        public void onClick(View view) {
            int color = ContextCompat.getColor(InsertCubeActivity.this.getBaseContext(), R.color.black);

            switch (side) {
                case sideTop:
                    color = ContextCompat.getColor(view.getContext(), R.color.red);
                    break;
                case sideLeft:
                    color = ContextCompat.getColor(view.getContext(), R.color.green);
                    break;
                case sideFront:
                    color = ContextCompat.getColor(view.getContext(), R.color.white);
                    break;
                case sideRight:
                    color = ContextCompat.getColor(view.getContext(), R.color.blue);
                    break;
                case sideBottom:
                    color = ContextCompat.getColor(view.getContext(), R.color.orange);
                    break;
                case sideBack:
                    color = ContextCompat.getColor(view.getContext(), R.color.yellow);
                    break;
            }

            Intent myIntent = new Intent(InsertCubeActivity.this, InsertSideActivity.class);
            myIntent.putExtra("color", color);
            InsertCubeActivity.this.startActivity(myIntent);
        }
    }
}
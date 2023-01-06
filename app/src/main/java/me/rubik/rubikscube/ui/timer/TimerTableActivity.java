package me.rubik.rubikscube.ui.timer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.database.DatabaseHandler;
import me.rubik.rubikscube.database.Times;
import me.rubik.rubikscube.databinding.ActivityTimerTableBinding;
import me.rubik.rubikscube.utils.Utils;

public class TimerTableActivity extends AppCompatActivity {

    private ActivityTimerTableBinding binding;

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerTableBinding.inflate(getLayoutInflater());
        initTable();
        addButtonListeners();
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Table");

    }

    private boolean sortByDate = true;

    private void addButtonListeners() {
        binding.buttonSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tableLayout.removeAllViews();
                    }
                });

                new Thread() {
                    @Override
                    public void run() {
                        List<Times> times = DatabaseHandler.getDatabase().getAll();
                        Comparator<Times> comparator;
                        if (sortByDate) {
                            comparator = (Comparator<Times>) (time1, time2) -> (int) (time1.time - time2.time);
                            binding.buttonSortBy.setText("SORT BY TIME");
                            sortByDate = false;
                        } else {
                            comparator = (Comparator<Times>) (time1, time2) -> (int) (time2.date - time1.date);
                            binding.buttonSortBy.setText("SORT BY Date");
                            sortByDate = true;
                        }
                        times.sort(comparator);
                        addListToTable(times);
                    }
                }.start();

            }
        });
    }

    private void initTable() {
        thread = new Thread() {
            @Override
            public void run() {
                List<Times> times = DatabaseHandler.getDatabase().getAll();
                Comparator<Times> comparator = (Comparator<Times>) (time1, time2) -> (int) (time2.date - time1.date);
                times.sort(comparator);
                addListToTable(times);
            }
        };
        thread.start();
    }

    private void addListToTable(List<Times> times) {
        addTableHeaders();
        Iterator<Times> iterator = times.iterator();
        while (iterator.hasNext()) {
            Times time = iterator.next();
            TableRow row = new TableRow(TimerTableActivity.this);
            TextView col1 = new TextView(TimerTableActivity.this);
            TextView col2 = new TextView(TimerTableActivity.this);

            col1.setText(Utils.formatTime(time.time));
            col1.setTextColor(0xFF000000);
            col1.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 2f));
            col1.setPadding(10, 10, 10, 10);
            col1.setTextSize(14);
            col1.setGravity(Gravity.CENTER_HORIZONTAL);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
            col2.setText(dateFormat.format(new Date(time.date)));
            col2.setTextColor(0xFF000000);
            col2.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 4f));
            col2.setPadding(10, 10, 10, 10);
            col2.setTextSize(14);
            col2.setGravity(Gravity.CENTER_HORIZONTAL);

            row.addView(col1);
            row.addView(col2);

            row.setOnClickListener(new TableOnClickListener(time, row));
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    binding.tableLayout.addView(row);
                }
            });
        }
    }

    private void addTableHeaders() {
        TableRow row = new TableRow(TimerTableActivity.this);
        TextView col1 = new TextView(TimerTableActivity.this);
        TextView col2 = new TextView(TimerTableActivity.this);

        col1.setText(getResources().getText(R.string.col1));
        col1.setTextColor(0xFFFFFFFF);
        col1.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 2f));
        col1.setPadding(10, 10, 10, 10);
        col1.setTextSize(14);
        col1.setGravity(Gravity.CENTER_HORIZONTAL);


        col2.setText(getResources().getText(R.string.col2));
        col2.setTextColor(0xFFFFFFFF);
        col2.setLayoutParams(new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 4f));
        col2.setPadding(10, 10, 10, 10);
        col2.setTextSize(14);
        col2.setGravity(Gravity.CENTER_HORIZONTAL);

        row.setBackgroundColor(0xffff4444);
        row.addView(col1);
        row.addView(col2);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tableLayout.addView(row);
            }
        });
    }

    private final class TableOnClickListener implements View.OnClickListener {

        private Times time;
        private View view;

        public TableOnClickListener(Times time, View view) {
            this.time = time;
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TimerTableActivity.this).setMessage("Are you sure you want to delete this?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                            new Thread() {
                                @Override
                                public void run() {
                                    DatabaseHandler.getDatabase().delete(time);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.tableLayout.removeView(view);
                                        }
                                    });
                                }
                            }.start();
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        }
                    })
                    .show();
        }
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

}
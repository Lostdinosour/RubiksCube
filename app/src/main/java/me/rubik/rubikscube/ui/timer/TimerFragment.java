package me.rubik.rubikscube.ui.timer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.database.DatabaseHandler;
import me.rubik.rubikscube.database.Times;
import me.rubik.rubikscube.database.TimesDao;
import me.rubik.rubikscube.databinding.FragmentTimerBinding;
import me.rubik.rubikscube.scrambler.Scrambler;

public class TimerFragment extends Fragment {

    private static FragmentTimerBinding binding;

    private long startClick;

    private static boolean timerStarted;

    private Handler handler = new Handler();

    private static FragmentActivity currentActivity;

    private static String lastScramble;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        currentActivity = getActivity();
        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!timerStarted) {
                        startClick = System.currentTimeMillis();
                        binding.textTimer.setTextColor(getActivity().getColor(R.color.red));
                    } else {
                        binding.textTimer.setTextColor(getActivity().getColor(R.color.black));
                        stopTimer();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!timerStarted && startClick != 0) {
                        if (System.currentTimeMillis() - startClick > 1500) {
                            startTimer();
                        } else {
                            binding.textTimer.setTextColor(getActivity().getColor(R.color.black));
                        }
                    }
                }
                return true;
            }
        });

        if (timerStarted) {
            binding.textTimer.setTextColor(getActivity().getColor(R.color.red));
        } else {
            binding.textTimer.setTextColor(getActivity().getColor(R.color.black));
        }
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        DatabaseHandler.getDatabase().deleteAll();
                    }
                }.start();
            }
        });
        regenScramble();
        return root;
    }

    private void regenScramble() {
        if (!timerStarted) {
            lastScramble = Scrambler.genScramble();
        }
        binding.textViewScramble.setText(lastScramble);

    }

    public void updateBestTime() {
        new Thread() {
            @Override
            public void run() {
                String textViewString = "Best time: " + formatTime(DatabaseHandler.getDatabase().getBestTime());
                binding.textViewBestTime.setText(textViewString);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int timePast;

    private void startTimer() {
        timerStarted = true;
        final long startTime = System.currentTimeMillis();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!timerStarted) {
                    return;
                }

                timePast = (int) (System.currentTimeMillis() - startTime);

                String time = formatTime(timePast);

                if (getForegroundFragment() instanceof TimerFragment) {
                    binding.textTimer.setText(time);
                }

                handler.postDelayed(this, 100);
            }
        });
    }

    private void stopTimer() {
        timerStarted = false;
        startClick = 0;
        new Thread() {
            @Override
            public void run() {
                TimesDao db = DatabaseHandler.getDatabase();

                Times time = new Times();
                time.setValues(System.currentTimeMillis(),timePast,lastScramble);
                db.insert(time);

                updateBestTime();
                regenScramble();
            }
        }.start();
    }

    public Fragment getForegroundFragment(){
        if (currentActivity == null) {
            return null;
        }
        Fragment navHostFragment = currentActivity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    public String formatTime(int time) {
        int minutes = ((time / 1000) / 60);
        time -= (minutes * 60) * 1000;
        int secs = (time / 1000);
        time -= secs * 1000;
        int miliSecs = time / 100;

        String formattedTime;
        if (minutes != 0) {
            formattedTime = minutes + ":" + secs + "." + miliSecs;
        } else {
            formattedTime = secs + "." + miliSecs;
        }

        return formattedTime;
    }

}
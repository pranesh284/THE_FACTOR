package com.example.the_factor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static final int KEY_INPUT = 1;
    private static final String SHARED_PREFS = "shared_prefs";
    private static final String PREFS_STREAK = "streak";
    private static final String PREFS_SCORE = "score";
    private int score, streak, highest_streak;
    TextView text_streak, text_score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_score = findViewById(R.id.textView_score1);
        text_streak = findViewById(R.id.textview_highscore);
        final Button button = findViewById(R.id.button_entergame);

        set_values();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_game();
            }
        });

    }

    private void start_game() {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivityForResult(intent, KEY_INPUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_INPUT) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                streak = data.getIntExtra(Main2Activity.STREAK, 0);
                score = data.getIntExtra(Main2Activity.SCORE, 0);
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(PREFS_SCORE, score);
                editor.apply();
                text_score.setText(String.format(Locale.getDefault(), "Last game score:%d", score));
                if (streak > highest_streak)
                    update_streak(editor);
            }

        }
    }

    private void update_streak(SharedPreferences.Editor editor) {


        highest_streak = streak;
        text_streak.setText(String.format(Locale.getDefault(), "Highest WINNING streak:%d", highest_streak));
        editor.putInt(PREFS_STREAK, streak);
        editor.apply();
    }

    private void set_values() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highest_streak = preferences.getInt(PREFS_STREAK, 0);
        score = preferences.getInt(PREFS_SCORE, 0);
        text_score.setText(String.format(Locale.getDefault(), "Last game score:%d", score));
        text_streak.setText(String.format(Locale.getDefault(), "Highest WINNING streak:%d", highest_streak));
    }
}

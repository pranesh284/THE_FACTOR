package com.example.the_factor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

import static android.R.color.background_dark;
import static android.R.color.holo_green_dark;
import static android.R.color.holo_red_dark;

public class Main2Activity extends AppCompatActivity {
    //string identifiers
    private static final long COUNTDOWN_MILLIS = 15000;
    public static final String STREAK = "winning_streak";
    public static final String SCORE = "score";
    public static final String INV_SC0RE = "score";
    public static final String INV_ANSWER = "answer";
    public static final String INV_STREAK = "streak";
    public static final String INV_COUNTDOWN_MILLIS = "count_down_left";
    public static final String INV_J = "int_j";

    //widgets
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    public View view;
    private RadioGroup radioGroup;
    private EditText editText;
    private TextView text_time;
    private TextView text_score;
    private TextView text_streak;
    private Random random = new Random();
    private int answer;
    private Vibrator vibrator;
    //stuff
    private int score, winning_streak, j;
    private long back_pressed_time;

    private CountDownTimer countDownTimer;
    private long timeleft_millis;

    public Main2Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        text_time = findViewById(R.id.textView_time);
        radioButton4 = findViewById(R.id.button4);
        radioButton1 = findViewById(R.id.button1);
        radioButton2 = findViewById(R.id.button2);
        radioButton3 = findViewById(R.id.button3);
        view = findViewById(R.id.view);
        text_score = findViewById(R.id.textView_score);
        text_streak = findViewById(R.id.textView_streak);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        radioGroup = findViewById(R.id.radiogroup);
        editText = findViewById(R.id.text_getnum);
        Button button = findViewById(R.id.button_entervalue);

        if (savedInstanceState == null) {
            score = 0;
            j = 1;
            winning_streak = 0;
        }
        if (savedInstanceState != null) {
            answer = savedInstanceState.getInt(INV_ANSWER);
            score = savedInstanceState.getInt(INV_SC0RE);
            winning_streak = savedInstanceState.getInt(INV_STREAK);
            timeleft_millis = savedInstanceState.getLong(INV_COUNTDOWN_MILLIS);
            j = savedInstanceState.getInt(INV_J);

            if (j == 0)
                start_countdown();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeleft_millis = COUNTDOWN_MILLIS;
                random = new Random();
                answer = random.nextInt(3) + 1;
                radioGroup.clearCheck();
                if (editText.getText().toString().equals(""))
                    Toast.makeText(Main2Activity.this, "please enter the number", Toast.LENGTH_SHORT).show();
                else if (j > 0) {
                    int num = Integer.parseInt(editText.getText().toString());
                    create_options(num, answer);
                    restore();
                    start_countdown();
                    j = 0;
                } else
                    Toast.makeText(Main2Activity.this, "Answer the previous question", Toast.LENGTH_SHORT).show();
            }
        });
        Button button1 = findViewById(R.id.button_answer);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j++;
                show_option_color();
                editText.setText(null);
            }
        });
    }

    private void create_options(int n, int ans) {

        int r1 = Math.abs(random.nextInt((factorial(n) + 100) - (factorial(n) - 100)) + (factorial(n) - 100));
        int r2 = Math.abs(random.nextInt((factorial(n) + 100) - (factorial(n) - 100)) + (factorial(n) - 100));
        for (int i = 0; i <= 5; i++) {
            if (r1 == r2)
                r1 = Math.abs(random.nextInt((factorial(n) + 100) - (factorial(n) - 100)) + (factorial(n) - 100));
            else if (r1 == factorial(n))
                r1 = Math.abs(random.nextInt((factorial(n) + 100) - (factorial(n) - 100)) + (factorial(n) - 100));
            else if (factorial(n) == r2)
                r2 = Math.abs(random.nextInt((factorial(n) + 100) - (factorial(n) - 100)) + (factorial(n) - 100));
        }
        switch (ans) {
            case 1:
                radioButton1.setText(String.valueOf(Math.abs(factorial(n))));
                radioButton2.setText(String.valueOf(Math.abs(r1)));
                radioButton3.setText(String.valueOf(Math.abs(r2)));
                break;
            case 2:
                radioButton2.setText(String.valueOf(Math.abs(factorial(n))));
                radioButton3.setText(String.valueOf(Math.abs(r2)));
                radioButton1.setText(String.valueOf(Math.abs(r1)));
                break;
            case 3:
                radioButton3.setText(String.valueOf(Math.abs(factorial(n))));
                radioButton2.setText(String.valueOf(Math.abs(r2)));
                radioButton1.setText(String.valueOf(Math.abs(r1)));
                break;
        }

    }//creates options once the number is entered

    private static int factorial(int num) {
        int fact = 1;
        if (num == 0) {
            return fact;
        } else {
            for (int i = 1; i <= num; i++)
                fact = fact * i;
            return fact;
        }
    }//gets the factorial of number

    private void show_option_color() {
        j++;
        if (is_answered()) {
            radioButton1.setTextColor(getResources().getColor(holo_red_dark));
            radioButton2.setTextColor(getResources().getColor(holo_red_dark));
            radioButton3.setTextColor(getResources().getColor(holo_red_dark));
            countDownTimer.cancel();
            switch (answer) {
                case 1:
                    radioButton1.setTextColor(getResources().getColor(holo_green_dark));
                    break;
                case 2:
                    radioButton2.setTextColor(getResources().getColor(holo_green_dark));
                    break;
                case 3:
                    radioButton3.setTextColor(getResources().getColor(holo_green_dark));
                    break;
            }
            set_background();
        } else {
            Toast.makeText(Main2Activity.this, "Please choose one option", Toast.LENGTH_SHORT).show();
        }
    }//shows the correct option once button is clicked

    private void set_background() {
        RadioButton answer_button = findViewById(radioGroup.getCheckedRadioButtonId());
        int ans = radioGroup.indexOfChild(answer_button) + 1;
        if (ans == answer) {
            view.setBackgroundColor(getResources().getColor(holo_green_dark));
            score += 4;
            ++winning_streak;
        } else {
            view.setBackgroundColor(getResources().getColor(holo_red_dark));
            score -= 1;
            winning_streak = 0;
            vibrator.vibrate(500);
        }
        set_scores();
    }//sets the background color after checking the answer

    private void restore() {
        radioButton1.setTextColor(radioButton4.getTextColors());
        radioButton2.setTextColor(radioButton4.getTextColors());
        radioButton3.setTextColor(radioButton4.getTextColors());
        view.setBackgroundColor(getResources().getColor(background_dark));
    }//restores all the colors

    private void start_countdown() {
        countDownTimer = new CountDownTimer(timeleft_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft_millis = millisUntilFinished;
                update_count_down();

            }

            @Override
            public void onFinish() {
                timeleft_millis = 0;
                update_count_down();
                check();
            }
        }.start();

    }//starts the timer

    private void update_count_down() {
        int seconds = (int) (timeleft_millis / 1000);
        String updated_time = String.format(Locale.getDefault(), "00:%02d", seconds);
        text_time.setText(updated_time);
        if (timeleft_millis <= 5000)
            text_time.setTextColor(getResources().getColor(holo_red_dark));
    }//displays time

    private void check() {
        if (is_answered())
            show_option_color();
        else
            finish_game();
    }//checks if button is not clicked

    private void set_scores() {
        text_score.setText(String.format(Locale.getDefault(), "Current score:%d", score));
        text_streak.setText(String.format(Locale.getDefault(), "Current streak:%d", winning_streak));
    }//prints the scores

    private void finish_game() {
        Intent new_intent = new Intent();
        new_intent.putExtra(STREAK, winning_streak);
        new_intent.putExtra(SCORE, score);
        setResult(RESULT_OK, new_intent);
        finish();

    }//returns the values and ends the activity

    @Override
    public void onBackPressed() {
        if (back_pressed_time + 2000 > System.currentTimeMillis())
            finish_game();
        else
            Toast.makeText(Main2Activity.this, "Press again to exit the game", Toast.LENGTH_SHORT).show();
        back_pressed_time = System.currentTimeMillis();
    }

    private Boolean is_answered() {
        return radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INV_SC0RE, score);
        outState.putInt(INV_STREAK, winning_streak);
        outState.putLong(INV_COUNTDOWN_MILLIS, timeleft_millis);
        outState.putInt(INV_ANSWER, answer);
        outState.putInt(INV_J, j);
    }
}

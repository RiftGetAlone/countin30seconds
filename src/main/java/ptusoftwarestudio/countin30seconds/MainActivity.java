package ptusoftwarestudio.countin30seconds;

import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView answerTitleView, bestScoreView, currentScoreView, exampleView, underExampleView;
    private Button button1, button2, button3;
    private ProgressBar progressBar;
    private Typeface typeface;
    private SharedPreferences sharedPreferences;
    private long startTime;
    private int rightAnswer = 0;
    private int currentScore;
    private int min;
    private int max;

    final Random random = new Random();
    private final int START_MIN = 4;
    private final int START_MAX = 14;
    private final int START_PROGRESS_BAR = 30;
    private final String BEST_SCORE = "best_score";

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            int timeLeft = ((int)((System.currentTimeMillis() - startTime) / 1000)) % 60;
            progressBar.setProgress(START_PROGRESS_BAR - timeLeft);
            if (START_PROGRESS_BAR - timeLeft <= 0) {
                gameOver(getString(R.string.time_is_over));
            }
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bestScoreView = findViewById(R.id.textBestScore);
        currentScoreView = findViewById(R.id.textCurrentScore);
        answerTitleView = findViewById(R.id.textAnswerBox);
        exampleView = findViewById(R.id.textExample);
        underExampleView = findViewById(R.id.textUnderExample);
        progressBar = findViewById(R.id.progressBar1);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        currentScore = 0;

        typeface = ResourcesCompat.getFont(this, R.font.josefinsans_bold);
        bestScoreView.setTypeface(typeface);
        currentScoreView.setTypeface(typeface);
        exampleView.setTypeface(typeface);
        button1.setTypeface(typeface);
        button2.setTypeface(typeface);
        button3.setTypeface(typeface);
        typeface = ResourcesCompat.getFont(this, R.font.josefinsans_regular);
        answerTitleView.setTypeface(typeface);
        underExampleView.setTypeface(typeface);
        setNewGame(getString(R.string.tap_to_start), "", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textExample:
                startNewGame();
                break;
            case R.id.button1:
                if (Integer.parseInt(button1.getText().toString()) == rightAnswer) {
                    addPoint();
                    setNewValues();
                }
                else {
                    gameOver(getString(R.string.wrong));
                }
                break;
            case R.id.button2:
                if (Integer.parseInt(button2.getText().toString()) == rightAnswer) {
                    addPoint();
                    setNewValues();
                }
                else {
                    gameOver(getString(R.string.wrong));
                }
                break;
            case R.id.button3:
                if (Integer.parseInt(button3.getText().toString()) == rightAnswer) {
                    addPoint();
                    setNewValues();
                }
                else {
                    gameOver(getString(R.string.wrong));
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        gameOver(getString(R.string.time_is_over));
    }

    private void saveBestScore(int newBestScore) {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor sPrefEd = sharedPreferences.edit();
        sPrefEd.putInt(BEST_SCORE, newBestScore);
        sPrefEd.apply();
    }

    private int loadBestScore() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getInt(BEST_SCORE, 0);
    }

    private void setNewGame(String exampleText, String underExampleText, String answerText) {
        min = START_MIN;
        max = START_MAX;
	  currentScore = 0;
		
        bestScoreView.setText(String.format(getString(R.string.best_score), loadBestScore()));
        currentScoreView.setText(getString(R.string.current_score_0));
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        answerTitleView.setText(answerText);
        progressBar.setVisibility(View.INVISIBLE);
        exampleView.setText(exampleText);
        exampleView.setClickable(true);
        underExampleView.setVisibility(View.VISIBLE);
        underExampleView.setText(underExampleText);
        answerTitleView.setVisibility(View.VISIBLE);
        answerTitleView.setText(answerText);
    }

    private void startNewGame() {
        setNewValues();
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        exampleView.setClickable(false);
        underExampleView.setVisibility(View.INVISIBLE);
        answerTitleView.setVisibility(View.VISIBLE);
        answerTitleView.setText(getString(R.string.the_answer_is));
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void gameOver(String message) {
        timerHandler.removeCallbacks(timerRunnable);
        if (currentScore > loadBestScore()) {
            saveBestScore(currentScore);
            setNewGame(getString(R.string.tap_to_start),
                    String.format(getString(R.string.your_score_record), currentScore,
                            getString(R.string.new_record)), message);
        }
        else {
            setNewGame(getString(R.string.tap_to_start),
                    String.format(getString(R.string.your_score), currentScore), message);
        }
    }

    private void addPoint() {
        currentScore += 1;
        currentScoreView.setText(String.format(getString(R.string.current_score),
                currentScore));
        min = START_MIN*currentScore;
        max = START_MAX*currentScore;
    }

    private void setNewValues() {
        int buttonId;
        int firstNumber;
        int secondNumber;
        int numberValue1;
        int numberValue2;

        firstNumber = getRandomInt();
        secondNumber = getRandomInt();
        rightAnswer = firstNumber + secondNumber;
        exampleView.setText(String.format("%d + %d", firstNumber, secondNumber));
        do {
            numberValue1 = getRandomAnswer();
        } while (numberValue1 == rightAnswer);
        do {
            numberValue2 = getRandomAnswer();
        } while((numberValue2 == numberValue1) || (numberValue2 == rightAnswer));
        buttonId = random.nextInt(2);
        switch(buttonId) {
            case 0:
                button1.setText(Integer.toString(rightAnswer));
                button2.setText(Integer.toString(numberValue1));
                button3.setText(Integer.toString(numberValue2));
                break;
            case 1:
                button2.setText(Integer.toString(rightAnswer));
                button1.setText(Integer.toString(numberValue1));
                button3.setText(Integer.toString(numberValue2));
                break;
            case 2:
                button3.setText(Integer.toString(rightAnswer));
                button1.setText(Integer.toString(numberValue1));
                button3.setText(Integer.toString(numberValue2));
                break;
        }
    }

    private int getRandomInt() {
        return min + random.nextInt(max - min);
    }

    private int getRandomAnswer() {
        int minV = rightAnswer - (int)(rightAnswer*0.2);
        int maxV = rightAnswer + (int)(rightAnswer*0.2);
        return minV + random.nextInt(maxV - minV);
    }
}


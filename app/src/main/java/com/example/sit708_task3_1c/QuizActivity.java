package com.example.sit708_task3_1c;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private ProgressBar progressBar;
    private Button submitButton;
    private TextView[] optionTextViews = new TextView[4];
    private int questionIndex = 0;
    private int score = 0;
    private TextView welcomeTextView;
    private int selectedOptionIndex = -1;

    private String[] questions = {
            "Question 1?",
            "Question 2?",
            "Question 3?",
            "Question 4?",
            "Question 5?"
    };

    private String[][] options = {
            {"Answer 1", "Answer 2", "Answer 3", "Answer 4"},
            {"Answer 1", "Answer 2", "Answer 3", "Answer 4"},
            {"Answer 1", "Answer 2", "Answer 3", "Answer 4"},
            {"Answer 1", "Answer 2", "Answer 3", "Answer 4"},
            {"Answer 1", "Answer 2", "Answer 3", "Answer 4"}
    };

    private String[] correctAnswers = {
            "Answer 1",
            "Answer 1",
            "Answer 1",
            "Answer 1",
            "Answer 1"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize UI components
        initializeViews();

        // Set listeners for option TextViews
        setOptionListeners();

        // Update UI for first question
        updateQuestion();

        // Set submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerSubmission();
            }
        });
    }

    private void initializeViews() {
        questionTextView = findViewById(R.id.questionTitleTextView);
        progressBar = findViewById(R.id.progressBar);
        submitButton = findViewById(R.id.submitButton);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // Option TextViews
        optionTextViews[0] = findViewById(R.id.option1TextView);
        optionTextViews[1] = findViewById(R.id.option2TextView);
        optionTextViews[2] = findViewById(R.id.option3TextView);
        optionTextViews[3] = findViewById(R.id.option4TextView);

        // Welcome message
        String userName = getIntent().getStringExtra("USER_NAME");
        welcomeTextView.setText(getString(R.string.welcome_user, userName));
    }

    private void setOptionListeners() {
        for (int i = 0; i < optionTextViews.length; i++) {
            final int index = i;
            optionTextViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionSelected(index);
                }
            });
        }
    }

    private void handleAnswerSubmission() {
        if (selectedOptionIndex == -1) {
            Toast.makeText(QuizActivity.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect = checkAnswer();

        if (questionIndex < questions.length - 1) {
            // Not the last question yet
            submitButton.setText(R.string.next_question);
            submitButton.setOnClickListener(v -> {
                // Reset options for next question
                resetOptionViews();
                questionIndex++;
                updateQuestion();
                submitButton.setText(R.string.submit); // Reset button text back to "Submit"

                submitButton.setOnClickListener(view -> handleAnswerSubmission());
            });
        } else if (questionIndex == questions.length - 1) {
            // Last question
            if (isCorrect || !isCorrect) {
                // Show results after the last question is answered
                submitButton.setText(R.string.finish_quiz);
                submitButton.setOnClickListener(v -> showResults());
            }
        }
    }


    private void onOptionSelected(int index) {
        selectedOptionIndex = index;
        for (int i = 0; i < optionTextViews.length; i++) {
            optionTextViews[i].setSelected(i == index);
        }
    }

    private boolean checkAnswer() {
        int correctIndex = Arrays.asList(correctAnswers).indexOf(correctAnswers[questionIndex]);
        boolean isCorrect = options[questionIndex][selectedOptionIndex].equals(correctAnswers[questionIndex]);

        for (int i = 0; i < optionTextViews.length; i++) {
            if (i == correctIndex) {
                optionTextViews[i].setBackgroundColor(ContextCompat.getColor(this, R.color.correct_answer_color));
            } else {
                optionTextViews[i].setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            }

            if (i == selectedOptionIndex && !isCorrect) {
                optionTextViews[i].setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_answer_color));
            }

            optionTextViews[i].setClickable(false); // Prevent more selections
        }

        if (isCorrect) {
            score++;
        }

        return isCorrect;
    }

    private void resetOptionViews() {
        for (TextView optionView : optionTextViews) {
            optionView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent)); // Reset background
            optionView.setClickable(true); // Make them clickable again
        }
    }

    private void updateQuestion() {
        questionTextView.setText(questions[questionIndex]);
        for (int i = 0; i < optionTextViews.length; i++) {
            optionTextViews[i].setText(options[questionIndex][i]);
            optionTextViews[i].setBackgroundResource(R.drawable.answer_selector);
            optionTextViews[i].setClickable(true);
            optionTextViews[i].setSelected(false);
        }
        progressBar.setProgress((questionIndex + 1) * 100 / questions.length);
        selectedOptionIndex = -1;
    }

    private void showResults() {
        Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL_QUESTIONS", questions.length);
        intent.putExtra("USER_NAME", getIntent().getStringExtra("USER_NAME"));
        startActivity(intent);
        finish();
    }
}

package com.matpacheco.flashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class ReviewActivity extends AppCompatActivity
{
    ConstraintLayout layout;
    int DECK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        layout = (ConstraintLayout)findViewById(R.id.constraintLayout);
        DECK_ID = Integer.parseInt(getIntent().getStringExtra("DECK_ID"));

        startReview();
    }

    private void startReview()
    {
        //If we're out of cards to review
        if(MainActivity.reviewable.get(DECK_ID).size() == 0)
        {
            finish();
            return;
        }

        //Reset view to white background and new random question to answer
        layout.setBackgroundColor(Color.rgb(255,255,255));
        TextView questionView = (TextView)findViewById(R.id.questionView);

        Random rnd = new Random();
        final int index = rnd.nextInt(((MainActivity.reviewable.size()-1) - 0) + 1 );

        final Item current = MainActivity.reviewable.get(DECK_ID).get(index);
        String firstQuestion = (String)current.getQuestion();
        questionView.setText(firstQuestion);

        //Handler for submit button and its two jobs
        final Button mButton = (Button) findViewById(R.id.button);
        mButton.setTag(1);
        mButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                final int status =(Integer) v.getTag();

                //If user has yet to check their answer to the question, button is to submit their answer
                if(status == 1)
                {
                    EditText editText = (EditText)findViewById(R.id.editText);
                    String user_answer = editText.getText().toString();
                    ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.constraintLayout);
                    if(user_answer.toLowerCase().equals(current.getAnswer().toLowerCase()))
                    {
                        //Set current Item as last item on ArrayList to optimize removal speed
                        Item temp = MainActivity.reviewable.get(DECK_ID).get(MainActivity.reviewable.size()-1);
                        MainActivity.reviewable.get(DECK_ID).set(MainActivity.reviewable.get(DECK_ID).size()-1,current);
                        MainActivity.reviewable.get(DECK_ID).set(index, temp);
                        MainActivity.reviewable.get(DECK_ID).remove(MainActivity.reviewable.size()-1);
                        current.setLevel(current.getLevel()+1);

                        layout.setBackgroundColor(Color.rgb(230,255,230));
                        mButton.setText("Next");
                    }
                    else
                    {
                        current.setLevel(current.getLevel()-1);
                        layout.setBackgroundColor(Color.rgb(255,230,230));
                        mButton.setText("Next");
                    }
                    v.setTag(0);

                    MainActivity.dbHelper.updateFlashcard(current);
                }
                //If user has already checked answer, button will display the next question
                else
                {
                    mButton.setText("Check Answer");
                    v.setTag(1);
                    startReview();
                }
            }
        });

    }
}

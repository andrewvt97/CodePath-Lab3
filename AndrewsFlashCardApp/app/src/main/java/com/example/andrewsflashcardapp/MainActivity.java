package com.example.andrewsflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView flashcard_question;
    TextView flashcard_answer;
    ImageView addButton;
    ImageView nextButton;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    int currentCardDisplayedIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcard_question = findViewById(R.id.flashcard_question);
        flashcard_answer = findViewById(R.id.flashcard_answer);
        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();


        flashcard_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcard_answer.setVisibility(View.VISIBLE);
                flashcard_question.setVisibility(View.INVISIBLE);
            }
        });

        flashcard_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcard_answer.setVisibility(View.INVISIBLE);
                flashcard_question.setVisibility(View.VISIBLE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent,100);

            }

        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (allFlashcards.size()==0)
                    return;

                currentCardDisplayedIndex++;


                if (currentCardDisplayedIndex>=allFlashcards.size()){
                    Snackbar.make(view,"You've reached the " +
                            "end of the cards, going back to start.",Snackbar.LENGTH_SHORT).show();
                    currentCardDisplayedIndex=0;
                }

                allFlashcards=flashcardDatabase.getAllCards();

                Flashcard currentCard = allFlashcards.get(currentCardDisplayedIndex);
                flashcard_question.setText(currentCard.getQuestion());
                flashcard_answer.setText(currentCard.getAnswer());

            }


        });






        if (allFlashcards!=null  && allFlashcards.size()>0){
            flashcard_question.setText(allFlashcards.get(0).getQuestion());
            flashcard_answer.setText(allFlashcards.get(0).getAnswer());
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            flashcard_question.setText(question);
            flashcard_answer.setText(answer);

            flashcardDatabase.insertCard(new Flashcard(question, answer));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }



}

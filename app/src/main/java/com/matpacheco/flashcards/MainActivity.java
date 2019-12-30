package com.matpacheco.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Item> items = new ArrayList<>();
//    static ArrayList<Item> reviewable = new ArrayList<>();
    static ArrayList<ArrayList<Item>> decks = new ArrayList<>();
    static ArrayList<ArrayList<Item>> reviewable = new ArrayList<>();
    static SQLiteHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new SQLiteHelper(this);

        LinearLayout layout = findViewById(R.id.Deck_List);
        Button deckButton = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
        deckButton.setPadding(50,50,10,50);

        Button deckButton2 = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
        deckButton2.setPadding(50,50,10,50);
        Button deckButton3 = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
        deckButton3.setPadding(50,50,10,50);
//        deckButton3.setElevation(500);


        layout.addView(deckButton);
        layout.addView(deckButton2);
        layout.addView(deckButton3);
        FloatingActionButton mCreateCard = (FloatingActionButton) findViewById(R.id.fabCreateCard);
        mCreateCard.setOnClickListener(v -> openDialog());
//                new View.OnClickListener(){
//            @Override
//            public void onClick(View view)
//            {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialog_create, null);
//                final EditText mSideA = (EditText)mView.findViewById(R.id.side_a);
//                final EditText mSideB = (EditText)mView.findViewById(R.id.side_b);
//                Button mCreate = (Button)mView.findViewById(R.id.create);
//
//                mCreate.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        if(!mSideA.getText().toString().isEmpty() && !mSideB.getText().toString().isEmpty())
//                        {
//                            Toast.makeText(MainActivity.this,
//                                    R.string.success_creation_msg,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            Toast.makeText(MainActivity.this,
//                                    R.string.failed_creation_message,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });

//        populateCardsLists();

//        TextView reviews_available = (TextView)findViewById(R.id.reviews_available);
//        reviews_available.setText("" + reviewable.size());

    }

    private void openDialog()
    {
        CardCreationDialog.display(getSupportFragmentManager());
    }

    private void populateCardsLists()
    {

//        Item item1 = new Item("My Name", "Matheus");
//        dbHelper.addFlashcard(item1);
//        Item item2 = new Item("Her Name", "Katrina");
//        Item item3 = new Item("My Age", "28");
//        Item item4 = new Item("Her Age", "22");
//
//        item1.setLevel(3);
//
//        items.add(item2);
//        items.add(item3);
//        items.add(item4);

        //Get list of items. Should be SORTED BY DECK
        items = dbHelper.getAllFlashcards();

        Calendar current_time = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));

        String previousDeck = "";
        int curr_index = -1;
        for (Item item: items ) 
        {
            if(!item.getDeck().equals(previousDeck))
            {
                decks.add(new ArrayList<Item>());
                reviewable.add(new ArrayList<Item>());
                curr_index++;
                previousDeck = item.getDeck();

                Button deckButton = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
                LinearLayout layout = findViewById(R.id.Deck_List);
                layout.addView(deckButton);
            }
            decks.get(curr_index).add(item);

            if(current_time.compareTo(item.getNext_review()) >= 0)
            {
                reviewable.get(curr_index).add(item);
            }
        }
    }

    /** Called when the user taps the View Cards button */
    public void viewCards(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, CardsList.class);
        startActivity(intent);
    }

    /** Called when the user taps the Review button */
    public void startReviews(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("DECK_ID", view.getId());
        startActivity(intent);

    }

}

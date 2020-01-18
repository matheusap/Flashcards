package com.matpacheco.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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

    static ArrayList<Item> items = new ArrayList<>(); //All Cards
    static ArrayList<ArrayList<Item>> decks = new ArrayList<>(); //All decks with respective cards
    static ArrayList<ArrayList<Item>> reviewable = new ArrayList<>(); //Reviewable decks with cards
    static ArrayList<String> deck_names = new ArrayList<>(); //Names of decks
    static SQLiteHelper dbHelper = null;
    private ArrayList<String> creation_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        dbHelper = new SQLiteHelper(this);
        LinearLayout layout = findViewById(R.id.Deck_List);

        /* TEMP CODE CREATING FAKE BUTTONS */
//        Button deckButton = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
//        deckButton.setPadding(50,50,10,50);
//        Button deckButton2 = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
//        deckButton2.setPadding(50,50,10,50);
//        Button deckButton3 = (Button)getLayoutInflater().inflate(R.layout.deckbutton,null);
//        deckButton3.setPadding(50,50,10,50);
//        layout.addView(deckButton);
//        layout.addView(deckButton2);
//        layout.addView(deckButton3);
        /* /FAKE CODE */

        FloatingActionButton mCreateCard = (FloatingActionButton) findViewById(R.id.fabCreateCard);
        mCreateCard.setOnClickListener(v -> openDialog());

        populateCardsLists();

//        TextView reviews_available = (TextView)findViewById(R.id.reviews_available);
//        reviews_available.setText("" + reviewable.size());

    }

    private void openDialog()
    {
        CardCreationDialog myDialog = new CardCreationDialog();
        myDialog.show(getSupportFragmentManager(), "Card Creation Dialog");
    }

    public void populateCardsLists()
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
        dbHelper.addFlashcard(new Item("What's Potato?","Batata","PT"));
        String previousDeck = "";
        int curr_index = -1;
        for (Item item: items ) 
        {
            if(!item.getDeck().equals(previousDeck))
            {
                deck_names.add(item.getDeck());
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

    public void createCards(String sideA, String sideB, String deck)
    {
        Item item = new Item(sideA, sideB, deck);
        dbHelper.addFlashcard(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //DETERMINE WHO STARTED THIS ACTIVITY
        String sender = null;
        if(this.getIntent().getExtras() != null)
            sender=this.getIntent().getExtras().getString("SENDER_KEY");

        //IF ITS THE FRAGMENT THEN RECEIVE DATA
        if(sender != null && sender.equals("CardCreation"))
        {
            this.receiveData();
            for(int i = 0; i < creation_list.size(); i+=3)
            {
                createCards(creation_list.get(i),creation_list.get(i+1),creation_list.get(i+3));
            }
        }
    }

    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        creation_list = getIntent().getStringArrayListExtra("creation_list");
    }



}

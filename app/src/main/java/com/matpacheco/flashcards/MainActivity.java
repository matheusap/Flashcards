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
    static ArrayList<ArrayList<Item>> decks = new ArrayList<>(); //All cards separated by deck
    static ArrayList<ArrayList<Item>> reviewable = new ArrayList<>(); //All reviewable decks separated by deck
    static ArrayList<String> deck_names = new ArrayList<>(); //Names of decks
    static SQLiteHelper dbHelper = null;
    private ArrayList<String> creation_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        dbHelper = new SQLiteHelper(this);
        LinearLayout layout = findViewById(R.id.Deck_List);

        FloatingActionButton mCreateCard = (FloatingActionButton) findViewById(R.id.fabCreateCard);
        mCreateCard.setOnClickListener(v -> openDialog());

        String sender = null;
        if(this.getIntent().getExtras() != null)
            return;
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
        //Get list of items. Should be SORTED BY DECK
        items = dbHelper.getAllFlashcards();

        Calendar current_time = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        String previousDeck = "";
        int curr_index = -1;
        int review_counter = 0;
        reviewable.clear();
        decks.clear();
        deck_names.clear();
        LinearLayout layout = findViewById(R.id.Deck_List);
        layout.removeAllViews();

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

                deckButton.setPadding(50,50,10,50);
                deckButton.setId(curr_index);
                deckButton.setText("No Reviews Available");
                deckButton.setEnabled(false);
                layout.addView(deckButton);
                review_counter = 0;
            }
            if(current_time.compareTo(item.getNext_review()) >= 0)
            {
                reviewable.get(curr_index).add(item);
                review_counter++;
                Button currButton = (Button) layout.getChildAt(curr_index);
                currButton.setText("Reviews Available\n" + review_counter);
                currButton.setEnabled(true);
            }
            decks.get(curr_index).add(item);


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
        int id = view.getId();
        intent.putExtra("DECK_ID", view.getId());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateCardsLists();
    }

    public void createCards(String sideA, String sideB, String deck)
    {
        dbHelper.addFlashcard(new Item(sideA,sideB,deck));
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
                createCards(creation_list.get(i),creation_list.get(i+1),creation_list.get(i+2));
            }

            creation_list.clear();
            populateCardsLists();
        }
    }

    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        creation_list = getIntent().getStringArrayListExtra("creation_list");
    }



}

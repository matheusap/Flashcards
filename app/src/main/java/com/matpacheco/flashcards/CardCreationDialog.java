package com.matpacheco.flashcards;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class CardCreationDialog extends DialogFragment
{
    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    private boolean cards_created = false;

    public static CardCreationDialog display(FragmentManager fragmentManager) {
        CardCreationDialog exampleDialog = new CardCreationDialog();
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.card_creation_dialog, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner deck_select = view.findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item, MainActivity.deck_names){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deck_select.setAdapter(dataAdapter);
        EditText deck_name_field = view.findViewById(R.id.new_deck_name);

        deck_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (Objects.equals(deck_select.getSelectedItem().toString(), "Create New Deck")) {
                    deck_name_field.setVisibility(View.VISIBLE);
                }
                else{
                    deck_name_field.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                deck_name_field.setVisibility(View.GONE);

            }
        });
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.card_creation_menu);

        toolbar.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.action_save:
                    //do some here
                    final EditText mSideA = view.findViewById(R.id.side_a);
                    final EditText mSideB = view.findViewById(R.id.side_b);
                    final Spinner mSpinner = view.findViewById(R.id.spinner);
                    final EditText mNewDeck = view.findViewById(R.id.new_deck_name);
                    String testA = mSideA.getText().toString();
                    String testB = mSideB.getText().toString();
                    if(!mSideA.getText().toString().isEmpty() && !mSideB.getText().toString().isEmpty() &&
                        !mSpinner.getSelectedItem().toString().equals("Select a Deck"))
                    {
                        if(mSpinner.getSelectedItem().toString().equals("Create New Deck") &&
                                !mNewDeck.getText().toString().isEmpty())
                        {
                            Toast.makeText( getActivity(),
                                    R.string.success_creation_msg,
                                    Toast.LENGTH_SHORT).show();
                            mSideA.setText("");
                            mSideB.setText("");
                            cards_created = true;
                        }
                        else if(!mSpinner.getSelectedItem().toString().equals("Create New Deck"))
                        {
                            Toast.makeText( getActivity(),
                                    R.string.success_creation_msg,
                                    Toast.LENGTH_SHORT).show();
                            mSideA.setText("");
                            mSideB.setText("");
                            cards_created = true;
                        }
                        else
                        {
                            Toast.makeText(getActivity(),
                                    R.string.failed_creation_message,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(),
                                R.string.failed_creation_message,
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return true;

        });
    }
}

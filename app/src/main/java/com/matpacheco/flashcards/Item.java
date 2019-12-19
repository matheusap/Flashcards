package com.matpacheco.flashcards;

import java.util.Calendar;
import java.util.TimeZone;

public class Item
{
    private int level;
    private Calendar next_review;
    private String question;
    private String answer;
    private String notes;
    private String deck;
    private int id;

    public Item()
    {
        level = -1;
        next_review = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
    }

    public Item(String question, String answer, String deck)
    {
        this.question = question;
        this.answer = answer;
        level = 0;
        next_review = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        this.deck = deck;
    }

    public Item(String question, String answer, String deck, String notes)
    {
        this.question = question;
        this.answer = answer;
        this.notes = notes;
        level = 0;
        next_review = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        this.deck = deck;
    }

    public int getLevel()
    {
        return level;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getAnswer()
    {
        return answer;
    }

    public String getNotes()
    {
        return notes;
    }

    public Calendar getNext_review()
    {
        return next_review;
    }

    public String getDeck()
    {
        return deck;
    }

    public int getId() { return id; }

    public void setLevel(int level)
    {
        if(this.level == -1)
        {
            this.level = level;
            return;
        }
        this.level = level;

        next_review.getTime();

        //New level has been set, now schedule next review
        switch(level)
        {
            case 1:
                //Next review in 4 hours
                next_review.add(Calendar.HOUR, 4);
                break;
            case 2:
                //Next review in 8 hours
                next_review.add(Calendar.HOUR, 8);
                break;
            case 3:
                //Next review in 1 day
                next_review.add(Calendar.DATE, 1);
                break;
            case 4:
                //Next review in 2 days
                next_review.add(Calendar.DATE, 2);
                break;
            case 5:
                //Next review in 4 days
                next_review.add(Calendar.DATE, 4);
                break;
            case 6:
                //Next review in 2 weeks
                next_review.add(Calendar.DATE, 14);
                break;
            case 7:
                //Next review in 1 month
                next_review.add(Calendar.DATE, 31);
                break;
            case 8:
                //Next review in 4 months
                next_review.add(Calendar.DATE, 122);
                break;
            case 9:
                //Burned
                next_review.add(Calendar.YEAR, 99);
                break;
        }
        //Round down the hour to make review timer cleaner and chunk reviews
        next_review.set(Calendar.MINUTE,0);
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setDeck(String deck)
    {
        this.deck = deck;
    }

    public void setNext_review(String next_review)
    {
        this.next_review.setTimeInMillis(Long.parseLong(next_review));
    }
}

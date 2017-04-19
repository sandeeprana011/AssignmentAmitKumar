package com.rana.assignment.models;

/**
 * Created by sandeeprana on 20/04/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class WordCountWrapper extends RowItem{
    private final String word;
    private final int wordCount;

    public WordCountWrapper(String word, int wordCount) {
        this.word = word;
        this.wordCount = wordCount;
    }

    public String getWord() {
        return word;
    }

    public int getWordCount() {
        return wordCount;
    }
}

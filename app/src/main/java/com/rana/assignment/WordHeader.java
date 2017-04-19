package com.rana.assignment;

import com.rana.assignment.models.RowItem;

/**
 * Created by sandeeprana on 20/04/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

class WordHeader extends RowItem {
    String headingText;

    public WordHeader(int minDigit, int maxDigit) {
        this.headingText = String.format("%d - %d", minDigit, maxDigit);
    }

    public String getHeadingText() {
        return headingText;
    }

    public void setHeadingText(String headingText) {
        this.headingText = headingText;
    }
}

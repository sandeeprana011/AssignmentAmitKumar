package com.rana.assignment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rana.assignment.models.RowItem;
import com.rana.assignment.models.WordCountWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sandeeprana on 20/04/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

class AdapterWordsAndCount extends RecyclerView.Adapter<AdapterWordsAndCount.ViewHolder> {
    private static final int HEADER = 0;
    private static final int WORD_VIEW = 1;


    private Context context;
    private ArrayList<RowItem> arrayList;
    private int lastDivident = 0;

    public AdapterWordsAndCount(Context context, ArrayList<RowItem> arrayList) {
        this.context = context;
        this.arrayList = new ArrayList<>();

        this.arrayList.add(0, new WordHeader(0, 10));
        for (RowItem wrapper : arrayList) {
            int count = ((WordCountWrapper) wrapper).getWordCount();
            int tempDivident = count / 10;
            if (lastDivident == tempDivident) {
            } else {
                lastDivident = tempDivident;
                int minDigit = lastDivident * 10;
                WordHeader wordHeader = new WordHeader(minDigit, minDigit + 10);
                this.arrayList.add(wordHeader);
            }
            this.arrayList.add(wrapper);

        }
    }

    @Override
    public AdapterWordsAndCount.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case WORD_VIEW:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_words_and_count, parent, false));
            case HEADER:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_label, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_words_and_count, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        RowItem count = arrayList.get(position);
        if (count instanceof WordCountWrapper) {
            return WORD_VIEW;
        } else {
            return HEADER;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case WORD_VIEW:
                WordCountWrapper wrapper = (WordCountWrapper) arrayList.get(position);
                holder.t_word.setText(wrapper.getWord());
                holder.t_count.setText(String.valueOf(wrapper.getWordCount()));
                break;
            case HEADER:
                WordHeader header = (WordHeader) arrayList.get(position);
                holder.t_label.setText(String.valueOf(header.getHeadingText()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.t_word)
        TextView t_word;
        @Nullable
        @BindView(R.id.t_count)
        TextView t_count;
        @Nullable
        @BindView(R.id.t_label)
        TextView t_label;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

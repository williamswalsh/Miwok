package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class    WordAdapter extends ArrayAdapter<Word> {
    private ArrayList<Word> words;
    private int colorRef;

    public WordAdapter(Context context, ArrayList<Word> words, int colorRef) {
        super(context, 0, words);

        // Storing Ref to color
        this.colorRef = colorRef;

        // Storing Ref to list of items
        this.words = words;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            // Inflater?
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        final Word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView englishTextView = (TextView) listItemView.findViewById(R.id.english_text_view);

        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        englishTextView.setText(currentWord.getEnglish());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);

        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        miwokTextView.setText(currentWord.getMiwok());

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        //ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image_view);

        if (currentWord.getImageRef() == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(currentWord.getImageRef());
        }

        LinearLayout textContainer = (LinearLayout) listItemView.findViewById(R.id.text_container);

        int color = ContextCompat.getColor(getContext(), colorRef);

        ImageView playButtonView = (ImageView) listItemView.findViewById(R.id.play_arrow);

        // Sets the background color of the Text container View and play button view
        textContainer.setBackgroundColor(color);
        playButtonView.setBackgroundColor(color);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}

package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersFragment extends Fragment {

    // AudioManager responsible for getting and abandoning AudioFocus
    private AudioManager audioManager;

    // MediaPlayer which can play mp3 and mp4
    private MediaPlayer mediaPlayer;


    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_list_view, container, false);

        final ArrayList<Word> numbers = new ArrayList<Word>();

        numbers.add(new Word(getString(R.string.one), "lutti", R.drawable.number_one, R.raw.number_one));
        numbers.add(new Word(getString(R.string.two), "otiiko", R.drawable.number_two, R.raw.number_two));
        numbers.add(new Word(getString(R.string.three), "tolookosu", R.drawable.number_three, R.raw.number_three));
        numbers.add(new Word(getString(R.string.four), "oyyisa", R.drawable.number_four, R.raw.number_four));
        numbers.add(new Word(getString(R.string.five), "massokka", R.drawable.number_five, R.raw.number_five));
        numbers.add(new Word(getString(R.string.six), "temmokka", R.drawable.number_six, R.raw.number_six));
        numbers.add(new Word(getString(R.string.seven), "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        numbers.add(new Word(getString(R.string.eight), "kawinta", R.drawable.number_eight, R.raw.number_eight));
        numbers.add(new Word(getString(R.string.nine), "wo’e", R.drawable.number_nine, R.raw.number_nine));
        numbers.add(new Word(getString(R.string.ten), "na’aacha", R.drawable.number_ten));                // App crashes on missing sound data

        // Create ArrayAdapter Point it to context and Words ArrayList
        WordAdapter adapter = new WordAdapter(getActivity(), numbers, R.color.category_numbers);

        // Get ListView.xml ref
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Associate adapter to ListView
        listView.setAdapter(adapter);


        // Can I have a single new AdapterView.OnItemClickListener()  used for all lists?
        // Set an Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Retrieve the AudioManager instance
                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                // Request AudioFocus
                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);


                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {

                    // Release mediaPlayer before playing another song
                    releaseMediaPlayer();

                    // Get data from current selection
                    Word word = numbers.get(position);

                    if( word.getMusicRef() != 0) {   // if music reference is assigned (unassigned/default value is 0)

                        // Create a MediaPlayer with the correct selection of music
                        mediaPlayer = MediaPlayer.create(getActivity(), word.getMusicRef());

                        // Setup an onCompletion Listener on the MediaPlayer so that we will release the mediaPlayer once
                        // the sound has finished playing
                        mediaPlayer.setOnCompletionListener(mpCompletionListener);

                        // start playing audio
                        mediaPlayer.start();
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the App goes into the Stopped State release the mediaPlayer
        releaseMediaPlayer();
    }

    // Created global variable instead of creating an anonymous class each time an Item is created
    // So one OnCompletionListener for all the MediaPlayer Instances
    private MediaPlayer.OnCompletionListener mpCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    // Listener which listens for an AudioFocusChange event
    public AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        // Interface method to be implemented to deal with changes in AudioFocus
        @Override
        public void onAudioFocusChange(int i) {

            if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // If the AudioFocus is lost temporarily or when its lost yet ducking is allowed

                // Pause the media player
                mediaPlayer.pause();

                // Change playback to beginning
                mediaPlayer.seekTo(0);
            } else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                // If you gain the audiofocus
                // Resume playing music
                mediaPlayer.start();
            } else if (i == AudioManager.AUDIOFOCUS_LOSS) {
                // When audioFocus is lost long term

                // Stop Audio playback
                mediaPlayer.stop();

                // Release the MediaPlayer resource
                releaseMediaPlayer();
            }
        }
    };

    /**
     * Clean up the media player by releasing its resources.
     */
    private final void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            // Release audioFocus when playback is complete
            audioManager.abandonAudioFocus(afChangeListener);
        }
    }
}

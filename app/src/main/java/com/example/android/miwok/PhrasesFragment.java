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

public class PhrasesFragment extends Fragment {

    // AudioManager responsible for getting and abandoning AudioFocus
    private AudioManager audioManager;

    // MediaPlayer which can play mp3 and mp4
    private MediaPlayer mediaPlayer;


    public PhrasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_list_view, container, false);

        final ArrayList<Word> phrases = new ArrayList<Word>();

        phrases.add(new Word("Where are you going?", "minto wuksus", 0, R.raw.phrase_where_are_you_going));
        phrases.add(new Word("What is your name?", "tinnә oyaase'nә", 0, R.raw.phrase_what_is_your_name));
        phrases.add(new Word("My name is...", "oyaaset...", 0, R.raw.phrase_my_name_is));
        phrases.add(new Word("How are you feeling?", "michәksәs?", 0, R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("I’m feeling good.", "kuchi achit", 0, R.raw.phrase_im_feeling_good));
        phrases.add(new Word("Are you coming?", "әәnәs'aa?", 0, R.raw.phrase_are_you_coming));
        phrases.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", 0, R.raw.phrase_yes_im_coming));
        phrases.add(new Word("I’m coming.", "әәnәm", 0, R.raw.phrase_im_coming));
        phrases.add(new Word("Let’s go.", "yoowutis", 0, R.raw.phrase_lets_go));
        phrases.add(new Word("Come here.", "әnni'nem", 0, R.raw.phrase_come_here));

        // Create ArrayAdapter Point it to context and Words ArrayList
        WordAdapter adapter = new WordAdapter(getActivity(), phrases, R.color.category_phrases);

        // Get ListView.xml ref
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Associate adapter to ListView
        listView.setAdapter(adapter);

        // Set an Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Retrieve the AudioManager instance
                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                // Request AudioFocus
                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);


                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // Release mediaPlayer before playing another song
                    releaseMediaPlayer();

                    // Get data from current selection
                    Word word = phrases.get(position);

                    // Create a MediaPlayer with the correct selection of music
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getMusicRef());

                    // Setup an onCompletion Listener on the MediaPlayer so that we will release the mediaPlayer once
                    // the sound has finished playing
                    mediaPlayer.setOnCompletionListener(mpCompletionListener);

                    // start playing audio
                    mediaPlayer.start();
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


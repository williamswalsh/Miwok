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

/**
 * Class which represents the business logic for the ColorsFragment of the MainActivity.
 *
 *
 * <p>
 * Author:      William Walsh
 * Version:     2.0 (Fragments)
 * Date:        18-05-18
 */
public class ColorsFragment extends Fragment {

    // AudioManager responsible for getting and abandoning AudioFocus
    private AudioManager audioManager;

    // MediaPlayer which can play mp3 and mp4
    private MediaPlayer mediaPlayer;

    public ColorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_view, container, false);

        /** TODO: Insert all the code from the NumberActivity’s onCreate() method after the setContentView method call */

        final ArrayList<Word> colors = new ArrayList<Word>();

        colors.add(new Word(getString(R.string.red), getString(R.string.red_miwok), R.drawable.color_red, R.raw.color_red));
        colors.add(new Word(getString(R.string.green), getString(R.string.green_miwok), R.drawable.color_green, R.raw.color_green));
        colors.add(new Word(getString(R.string.brown), getString(R.string.brown_miwok), R.drawable.color_brown, R.raw.color_brown));
        colors.add(new Word(getString(R.string.gray), getString(R.string.gray_miwok), R.drawable.color_gray, R.raw.color_gray));
        colors.add(new Word(getString(R.string.black), getString(R.string.black_miwok), R.drawable.color_black, R.raw.color_black));
        colors.add(new Word(getString(R.string.white), getString(R.string.white_miwok), R.drawable.color_white, R.raw.color_white));
        colors.add(new Word(getString(R.string.dusty_yellow), getString(R.string.dusty_yellow_miwok), R.drawable.color_dusty_yellow));
        colors.add(new Word(getString(R.string.mustard_yellow), getString(R.string.mustard_yellow_miwok), R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        // Create ArrayAdapter Point it to context and colors ArrayList
        WordAdapter colorAdapter = new WordAdapter(getActivity(), colors, R.color.category_colors);

        // Get ListView.xml ref
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Associate adapter to ListView
        listView.setAdapter(colorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Get AudioManager
                audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                // Request AudioFocus using the local audioManager instance
                // pass in the AudioFocusChangeListener, Stream type and AudioFocus Type
                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);

                // If the AudioFocus request is granted
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // Release mediaPlayer before playing another song
                    releaseMediaPlayer();

                    // Get the data associated with the list element selected using the position index
                    Word word = colors.get(position);

                    // register media buttons

                    // Create MediaPlayer
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getMusicRef());

                    // Set the onCompletionListener
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

        // When the App goes into the Stopped State release the
        releaseMediaPlayer();
    }

    // Listener which listens for when media playback completes
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

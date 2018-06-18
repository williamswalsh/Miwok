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
 * Class which represents the business logic for the FamilyFragment of the MainActivity.
 *
 * <p>
 * Author:      William Walsh
 * Version:     2.0 (Fragments)
 * Date:        18-05-18
 */
public class FamilyFragment extends Fragment {

    // AudioManager responsible for getting and abandoning AudioFocus
    private AudioManager audioManager;

    // MediaPlayer which can play mp3 and mp4
    private MediaPlayer mediaPlayer;

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_list_view, container, false);

        // Collection of words to store the list element data
        final ArrayList<Word> familyMembers = new ArrayList<Word>();

        // Adding members to the Collection
        familyMembers.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        familyMembers.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        familyMembers.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        familyMembers.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        familyMembers.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyMembers.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyMembers.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyMembers.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyMembers.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyMembers.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));


        // Create ArrayAdapter Point it to context, familyMember ArrayList & list element color
        WordAdapter adapter = new WordAdapter(getActivity(), familyMembers, R.color.category_family);

        // Get ListView.xml ref
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Associate adapter to ListView
        listView.setAdapter(adapter);

        // Set an Item Click listener
        // Pass in an anonymous concrete class
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
                    Word word = familyMembers.get(position);

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


    // Concrete class to implement the OnCompletionListener interface
    private MediaPlayer.OnCompletionListener mpCompletionListener = new MediaPlayer.OnCompletionListener() {

        // Upon completion of music playback on MediaPlayer
        @Override
        public void onCompletion(MediaPlayer mp) {

            // Release the MediaPlayer resource
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

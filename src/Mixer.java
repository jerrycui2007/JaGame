import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Mixer is the class for loading and playing sounds
 * @author Vincent Han
 */
public class Mixer {

    private ArrayList<Sound> activeSounds = new ArrayList<>();
    // Master volume for all sounds
    private float masterVolume = 1.0f;

    /**
     * Sets the global volume for all sounds
     * Multiplies all sound's volume by a set amount
     * @param volumeScalar the amount to multiply sound's volume by
     */
    public void setMasterVolume(float volumeScalar) {
        if (volumeScalar < 0){
            masterVolume = 0;
        } else if (volumeScalar > 100){
            masterVolume = 100;
        } else {
            masterVolume = volumeScalar;
        }
        for (Sound sound : activeSounds){
            sound.setVolume(sound.getVolume() * volumeScalar);
        }
    }

    /**
     * Gets the current master volume
     * @return masterVolume
     */
    public float getMasterVolume(){
        return masterVolume;
    }

    /**
     * Adds a sound to the active list
     * @param sound the sound to add to the list
     */
    public void addSound(Sound sound) {
        if (!activeSounds.contains(sound)) {
            activeSounds.add(sound);
        }
    }

    /**
     * Pauses all sounds in the active list
     */
    public void pauseAll(){
        for (Sound sound : activeSounds) {
            sound.pause();
        }
    }

    /**
     * Unpauses all sounds in the active list
     */
    public void unpauseAll() {
        for (Sound sound: activeSounds) {
            sound.unpause();
        }
    }

    /**
     * Stops all active sounds and removes them from the list
     */
    public void stopAll() {
        for (Sound sound : activeSounds) {
            sound.stop();
        }
        activeSounds.clear();
    }

    /**
     * Plays background music, looping it infinitely
     * @param backgroundMusic the music to play in the background
     */
    public void playBackgroundMusic(Music backgroundMusic){
        backgroundMusic.loop();
    }

    /**
     * Sound is the inner class representing a sound object to be played
     * Used for short sound effects
     */
    public static class Sound {
        // Volume ranges between 0 and 1, defaults to 1 unless otherwise specified
        private float volume;
        private Clip clip;
        private AudioInputStream audioStream;
        private boolean isPlaying;

        public Sound(String filepath){
            // Try to open the sound file
            try{
                audioStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } catch (Exception e) {
                System.out.println("Failed to open");
            }
            volume = 1.0f;
            isPlaying = false;
        }

        public Sound(String filepath, float volume){
            // Try to open the sound file
            try{
                audioStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } catch (Exception e) {
                System.out.println("Failed to open");
            }
            // Handle bad volume inputs
            if (volume < 0) {
                this.volume = 0;
            } else if (volume > 1) {
                this.volume = 1;
            } else {
                this.volume = volume;
            }
            isPlaying = false;
        }

        /**
         * Gets the current volume of the sound object
         * @return volume
         */
        public float getVolume(){
            return volume;
        }

        /**
         * Sets the volume of a sound object
         * If volume given is below 0, volume will be set to 0.
         * If volume given is above 1, volume will be set to 1.
         * @param volume a decimal number between 0 and 1
         */
        public void setVolume(float volume){
            if (volume < 0) {
                this.volume = 0;
            } else if (volume > 1) {
                this.volume = 1;
            } else {
                this.volume = volume;
            }
        }

        /**
         * Plays the sound file from the beginning of the clip
         */
        public void play(){
            if (!isPlaying){
                clip.setMicrosecondPosition(0);
                clip.start();
                isPlaying = true;
            } else if (!isActive()){
                stop();
            }
        }

        /**
         * Checks whether or not sound is actively capturing or rendering sound
         * @return true if sound is playing, false if not
         */
        public boolean isActive(){
            return clip.isActive();
        }

        /**
         * Stops playing the sound file and sets it back to the beginning of the clip
         */
        public void stop() {
            // Set back to the beginning of clip and stop it
            clip.setMicrosecondPosition(0);
            clip.stop();
            isPlaying = false;
        }

        /**
         * Stops playing the sound file without going back to the start of the file
         */
        public void pause(){
            if (isPlaying){
                clip.stop();
                isPlaying = false;
            }
        }

        /**
         * Resumes playing the sound from where it got paused
         */
        public void unpause(){
            if (!isPlaying){
                clip.start();
                isPlaying = true;
            }
        }

        /**
         * Gets the length of the sound in seconds
         * @return length of the sound in seconds
         */
        public long getSecondLength(){
            return clip.getMicrosecondLength() / 1000000;
        }

        /**
         * Gets the length of the sound in sample frames
         * @return length of sound in sample frames
         */
        public int getFrameLength(){
            return clip.getFrameLength();
        }

        /**
         * Resets the sound's position to the beginning of the sound
         */
        public void rewind(){
            clip.setMicrosecondPosition(0);
        }

        /**
         * Gradually reduces the volume of a sound to silence over a certain period of time
         * @param milliseconds the amount of time for the sound to fade out
         */
        public void fadeOut(int milliseconds){
            new Thread(() -> {
                float startVolume = getVolume();
                // Number of steps to fade smoothly
                int steps = 50;
                // The duration of each step
                float stepDuration = milliseconds / (float) steps;
                // The volume to decrease at each step
                float volumeStep = startVolume / steps;

                for (int i = 0; i < steps; i++){
                    setVolume(startVolume - (i*volumeStep));
                    try {
                        Thread.sleep((long) stepDuration);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Stop the clip after fading out
                stop();
            }).start();
        }
    }

    /**
     * Music is the inner class representing a music object to be played
     * Used for longer tracks like background music. Includes all methods from Sound but can also loop and set playback position
     */
    public static class Music {
        private Clip clip;
        private float volume;
        private AudioInputStream audioStream;
        private boolean isPlaying;

        public Music (String filepath){
            // Try to open the sound file
            try{
                audioStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } catch (Exception e) {
                System.out.println("Failed to open");
            }
            volume = 1.0f;
            isPlaying = false;
        }
        public Music (String filepath, float volume){
            // Try to open the sound file
            try{
                audioStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } catch (Exception e) {
                System.out.println("Failed to open");
            }
            // Handle bad volume inputs
            if (volume < 0) {
                this.volume = 0;
            } else if (volume > 1) {
                this.volume = 1;
            } else {
                this.volume = volume;
            }
            isPlaying = false;
        }

        /**
         * Gets the current volume of the music object
         * @return volume
         */
        public float getVolume(){
            return volume;
        }

        /**
         * Sets the volume of a music object
         * If volume given is below 0, volume will be set to 0.
         * If volume given is above 1, volume will be set to 1.
         * @param volume a decimal number between 0 and 1
         */
        public void setVolume(float volume){
            if (volume < 0) {
                this.volume = 0;
            } else if (volume > 1) {
                this.volume = 1;
            } else {
                this.volume = volume;
            }
        }

        /**
         * Plays the music file from the beginning of the clip
         */
        public void play(){
            if (!isPlaying){
                clip.setMicrosecondPosition(0);
                clip.start();
                isPlaying = true;
            } else if (!isActive()){
                stop();
            }
        }

        /**
         * Checks whether or not sound is actively capturing or rendering sound
         * @return true if music is playing, false if not
         */
        public boolean isActive(){
            return clip.isActive();
        }

        /**
         * Stops playing the music file and sets it back to the beginning of the clip
         */
        public void stop() {
            // Set back to the beginning of clip and stop it
            clip.setMicrosecondPosition(0);
            clip.stop();
            isPlaying = false;
        }

        /**
         * Loops the music file, going back to the start of the file once reaching the end.
         * Music will loop indefinitely until interrupted
         */
        public void loop(){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying = true;
        }

        /**
         * Loops the music file, going back to the start of the file once reaching the end.
         * @param loops the number of times to loop the music
         */
        public void loop(int loops){
            clip.loop(loops);
            isPlaying = true;
        }

        /**
         * Stops playing the music file without going back to the start of the file
         */
        public void pause(){
            if (isPlaying){
                clip.stop();
                isPlaying = false;
            }
        }

        /**
         * Resumes playing the music from where it got paused
         */
        public void unpause(){
            if (!isPlaying){
                clip.start();
                isPlaying = true;
            }
        }

        /**
         * Gets the length of the music in seconds
         * @return length of the music in seconds
         */
        public long getSecondLength(){
            return clip.getMicrosecondLength() / 1000000;
        }

        /**
         * Gets the length of the music in sample frames
         * @return length of music in sample frames
         */
        public int getFrameLength(){
            return clip.getFrameLength();
        }

        /**
         * Sets the position of the music in sample frames
         * @param frames the frame to position the music, must be between 0 and the length
         */
        public void setFramePosition(int frames){
            int length = getFrameLength();
            if (frames < 0 || frames > length){
                throw new IllegalArgumentException("Must be within the frame length");
            }
            clip.setFramePosition(frames);
        }

        /**
         * Sets the position of the music in microseconds
         * @param microseconds the microsecond to position the music, must be between 0 and the length
         */
        public void setMicrosecondPosition(long microseconds){
            long length = clip.getMicrosecondLength();
            if (microseconds < 0 || microseconds > length){
                throw new IllegalArgumentException("Must be within the length");
            }
            clip.setMicrosecondPosition(microseconds);
        }

        /**
         * Resets the music's position to the beginning of the music
         */
        public void rewind(){
            clip.setMicrosecondPosition(0);
        }

        /**
         * Gradually reduces the volume of the music to silence over a certain period of time
         * @param milliseconds the amount of time for the music to fade out
         */
        public void fadeOut(int milliseconds){
            new Thread(() -> {
                float startVolume = getVolume();
                // Number of steps to fade smoothly
                int steps = 50;
                // The duration of each step
                float stepDuration = milliseconds / (float) steps;
                // The volume to decrease at each step
                float volumeStep = startVolume / steps;

                for (int i = 0; i < steps; i++){
                    setVolume(startVolume - (i*volumeStep));
                    try {
                        Thread.sleep((long) stepDuration);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Stop the clip after fading out
                stop();
            }).start();
        }
    }

    /**
     * MusicQueue is the inner class for representing a playlist of Music objects to be played
     */
    public static class MusicQueue {
        private Queue<Music> playlist;
        private Music currentSong;

        public MusicQueue() {
            playlist = new LinkedList<>();
        }

        /**
         * Adds a Music object to the queue
         * @param music the music to add
         */
        public void addToQueue(Music music) {
            if (music != null){
                playlist.add(music);
            } else {
                throw new IllegalArgumentException("Music object cannot be null");
            }
        }

        /**
         * Plays the next track in the queue if it exists
         */
        public void playNext(){
            stopCurrentTrack();

            if (!playlist.isEmpty()){
                currentSong = playlist.poll();
                currentSong.play();
            } else {
                System.out.println("The playlist is empty");
                currentSong = null;
            }
        }

        /**
         * Stops playing the current track in the playlist
         */
        public void stopCurrentTrack(){
            if (currentSong != null && currentSong.isActive()){
                currentSong.pause();
            }
        }

        /**
         * Resumes the current track in the playlist
         */
        public void resumeCurrentTrack(){
            if (currentSong != null && !currentSong.isActive()){
                currentSong.unpause();
            }
        }

        /**
         * Clear the playlist queue
         */
        public void clearQueue(){
            playlist.clear();
        }

        /**
         * Checks if queue is empty
         * @return true if queue is empty, otherwise false
         */
        public boolean isQueueEmpty(){
            return playlist.isEmpty();
        }

        /**
         * Gets the number of tracks in the playlist
         * @return integer representing the number of tracks in the playlist
         */
        public int getQueueSize(){
            return playlist.size();
        }

        /**
         * Gets the currently playing Music object in the queue
         * @return Music object that is currently playing
         */
        public Music getCurrentSong(){
            return currentSong;
        }
    }
}

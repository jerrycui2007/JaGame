import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Mixer is the class for loading and playing sounds
 */
public class Mixer {

    /**
     * Sound is the inner class representing a sound object to be played
     */
    public class Sound {
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
         * Loops the sound file, going back to the start of the file once reaching the end.
         * Sound will loop indefinitely until interrupted
         * Only loops if sound file isn't already playing
         */
        public void loop(){
            if (!isPlaying){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                isPlaying = true;
            }
        }

        /**
         * Loops the sound file, going back to the start of the file once reaching the end.
         * Only loops if sound file isn't already playing
         * @param loops the number of times to loop the sound
         */
        public void loop(int loops){
            if (!isPlaying){
                clip.loop(loops);
                isPlaying = true;
            }
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
         * Sets the position of the sound in sample frames
         * @param frames the frame to position the sound, must be between 0 and the length
         */
        public void setFramePosition(int frames){
            int length = getFrameLength();
            if (frames < 0 || frames > length){
                throw new IllegalArgumentException("Must be within the frame length");
            }
            clip.setFramePosition(frames);
        }

        /**
         * Sets the position of the sound in microseconds
         * @param microseconds the microsecond to position the sound, must be between 0 and the lengthh
         */
        public void setMicrosecondPosition(long microseconds){
            long length = clip.getMicrosecondLength();
            if (microseconds < 0 || microseconds > length){
                throw new IllegalArgumentException("Must be within the length");
            }
            clip.setMicrosecondPosition(microseconds);
        }
    }
}

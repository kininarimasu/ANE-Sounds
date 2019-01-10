package com.digitalstrawberry.nativeExtensions.anesounds;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.media.SoundPool;

/**
 * Thread for playing sounds
 *
 * @author soh#zolex
 *
 */
public class SoundThread extends Thread {

    public BlockingQueue<Integer> sounds = new LinkedBlockingQueue<Integer>();
    public boolean stop = false;

    private SoundPool soundPool;

    /**
     * Constructor
     *
     * @param soundPool
     */
    public SoundThread(SoundPool soundPool) {

        this.soundPool = soundPool;
    }

    @Override
    /**
     * Wait for sounds to play
     */
    public void run() {

        try {

            int soundId;
            while (!this.stop) {

                soundId = this.sounds.take();

                if (soundId == -1) {

                    this.stop = true;
                    break;
                }

                this.soundPool.play(
                    soundId, // soundID
                    1.0f, // leftVolume
                    1.0f, // rightVolume
                    1, // priority
                    0, // loop
                    1.0f // rate
                );
            }

        } catch (InterruptedException e) {}
    }
}
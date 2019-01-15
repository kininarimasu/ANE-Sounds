package com.digitalstrawberry.nativeExtensions.anesounds;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.media.SoundPool;

public class SoundThread extends Thread {

    private BlockingQueue<Object> commands = new LinkedBlockingQueue<Object>();
    private SoundPool soundPool;

    public SoundThread(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    public void playSound(int soundId) {
        this.commands.add(soundId);
    }

    public void release() {
        this.commands.add("exit");
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object command = this.commands.take();

                if (command.equals("exit")) {
                    return;
                }

                int soundId = (Integer)command;

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
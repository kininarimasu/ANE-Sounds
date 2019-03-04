package com.digitalstrawberry.nativeExtensions.anesounds;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import java.util.Map;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicThread extends Thread {

    private static final String TAG = "MusicThread";

    private BlockingQueue<ThreadCommand> commands = new LinkedBlockingQueue<ThreadCommand>();
	private Map<Integer, MediaPlayer> mediaPlayers = new HashMap<Integer, MediaPlayer>();
    private int musicId = 0;

    public int loadMusic(final String path) {
        musicId++;
        final int id = musicId;
        commands.add(new ThreadCommand() {
            public int getId() {
                return id;
            }
            public void run() throws InterruptedException {
                AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(attributes);
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.e(TAG, "MediaPlayer.onError(): what = " + what + ", extra = " + extra);
                        mp.release();
                        mediaPlayers.remove(id);
                        return true;
                    }
                });

                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    mediaPlayer.release();
                    return;
                }

                mediaPlayer.setLooping(true);
                mediaPlayers.put(id, mediaPlayer);
            }
        });
        return id;
    }

    public void unloadMusic(final int id) {
        commands.add(new ThreadCommand() {
            public int getId() {
                return id;
            }
            public void run() throws InterruptedException {
                MediaPlayer mediaPlayer = mediaPlayers.remove(id);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        });
    }

    public void playMusic(final int id) {
        commands.add(new ThreadCommand() {
            public int getId() {
                return id;
            }
            public void run() throws InterruptedException {
                MediaPlayer mediaPlayer = mediaPlayers.get(id);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });
    }

    public void pauseMusic(final int id) {
        commands.add(new ThreadCommand() {
            public int getId() {
                return id;
            }
            public void run() throws InterruptedException {
                MediaPlayer mediaPlayer = mediaPlayers.get(id);
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
            }
        });
    }

    public void stopMusic(final int id) {
        commands.add(new ThreadCommand() {
            public int getId() {
                return id;
            }
            public void run() throws InterruptedException {
                MediaPlayer mediaPlayer = mediaPlayers.get(id);
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        });
    }

    public void release() {
        commands.add(new ThreadCommand() {
            public int getId() {
                return 0;
            }
            public void run() throws InterruptedException {
                for (Map.Entry<Integer, MediaPlayer> item : mediaPlayers.entrySet()) {
                    item.getValue().release();
                }
                mediaPlayers.clear();
                throw new InterruptedException();
            }
        });
    }

    @Override
    public void run() {
        try {
            while (true) {
                ThreadCommand command = commands.take();
                // try {
                    command.run();
                // } catch (IllegalStateException e) {
                //     Log.e(TAG, e.toString());
                //     e.printStackTrace();
                //     unloadMusic(command.getId());
                // }
            }
        } catch (InterruptedException e) {}
    }
}
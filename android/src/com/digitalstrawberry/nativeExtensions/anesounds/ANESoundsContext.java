package com.digitalstrawberry.nativeExtensions.anesounds;

import android.media.SoundPool;
import android.media.MediaPlayer;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREWrongThreadException;

import com.digitalstrawberry.nativeExtensions.anesounds.functions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ANESoundsContext extends FREContext
{
	public SoundPool soundPool;

	private SoundThread _soundThread;

	public SoundThread getSoundThread() {
		if (disposed) {
			return null;
		}
		if (_soundThread == null && soundPool != null) {
			_soundThread = new SoundThread(soundPool);
			_soundThread.start();
		}
		return _soundThread;
	}

	private MusicThread _musicThread;

	public MusicThread getMusicThread() {
		if (disposed) {
			return null;
		}
		if (_musicThread == null) {
			_musicThread = new MusicThread();
			_musicThread.start();
		}
		return _musicThread;
	}

    // Sound id mapped to a list of stream ids
    //public Map<Integer, List<Integer>> soundStreams;

	ANESoundsContext()
	{

	}

	private boolean disposed = false;

	@Override
	public void dispose()
	{
		disposed = true;

		if (soundPool != null)
		{
			soundPool.release();
			soundPool = null;
		}

		if (_soundThread != null)
		{
			_soundThread.release();
			_soundThread = null;
		}

		if (_musicThread != null)
		{
			_musicThread.release();
			_musicThread = null;
		}
	}

	@Override
	public Map<String, FREFunction> getFunctions()
	{
		Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();

		functionMap.put("initialize", new Initialize());
		functionMap.put("loadSound", new LoadSound());
		functionMap.put("playSoundFast", new PlaySoundFast());
		functionMap.put("playSound", new PlaySound());
		functionMap.put("playSoundLoop", new PlaySoundLoop());
		functionMap.put("unloadSound", new UnloadSound());
		functionMap.put("stopStream", new StopStream());
		functionMap.put("setVolume", new SetVolume());

		functionMap.put("loadMusic", new MusicPlayer() {
			public FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException {
				String path = args[0].getAsString();
				return FREObject.newObject(context.getMusicThread().loadMusic(path));
			}
		});
		functionMap.put("unloadMusic", new MusicPlayer() {
			public FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException {
				int id = args[0].getAsInt();
				context.getMusicThread().unloadMusic(id);
				return null;
			}
		});
		functionMap.put("playMusic", new MusicPlayer() {
			public FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException {
				int id = args[0].getAsInt();
				// boolean looping = args[1].getAsBool();
				context.getMusicThread().playMusic(id/*, looping*/);
				return null;
			}
		});
		functionMap.put("stopMusic", new MusicPlayer() {
			public FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException {
				int id = args[0].getAsInt();
				context.getMusicThread().stopMusic(id);
				return null;
			}
		});
		functionMap.put("muteMusic", new MusicPlayer() {
			public FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException {
				int id = args[0].getAsInt();
				context.getMusicThread().muteMusic(id);
				return null;
			}
		});
		functionMap.put("unmuteMusic", new MusicPlayer() {
			public FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException {
				int id = args[0].getAsInt();
				context.getMusicThread().unmuteMusic(id);
				return null;
			}
		});

		return functionMap;
	}
}

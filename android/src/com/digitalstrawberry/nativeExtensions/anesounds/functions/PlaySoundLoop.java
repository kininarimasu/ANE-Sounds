package com.digitalstrawberry.nativeExtensions.anesounds.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.digitalstrawberry.nativeExtensions.anesounds.ANESoundsContext;

public class PlaySoundLoop implements FREFunction
{
	@Override
	public FREObject call( FREContext context, FREObject[] args )
	{
		ANESoundsContext soundsContext = (ANESoundsContext) context;

		try
		{
			int soundId = args[0].getAsInt();
			int streamId = soundsContext.soundPool.play(soundId, 1.0f, 1.0f, 1, 1, 1.0f);
            return FREObject.newObject(streamId);
		}
		catch(Exception e)
		{
            e.printStackTrace();
		}

		return null;
	}
}

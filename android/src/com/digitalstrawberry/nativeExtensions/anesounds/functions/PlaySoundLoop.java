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
			int streamId = soundsContext.soundPool.play(
				soundId, // soundID
				1.0f, // leftVolume
				1.0f, // rightVolume
				1, // priority
				-1, // loop
				1.0f // rate
			);
            return FREObject.newObject(streamId);
		}
		catch(Exception e)
		{
            e.printStackTrace();
		}

		return null;
	}
}

package com.digitalstrawberry.nativeExtensions.anesounds.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.digitalstrawberry.nativeExtensions.anesounds.ANESoundsContext;

public class PlaySoundFast implements FREFunction
{
	@Override
	public FREObject call( FREContext context, FREObject[] args )
	{
		ANESoundsContext soundsContext = (ANESoundsContext) context;

		try
		{
			int soundId = args[0].getAsInt();
			soundsContext.getSoundThread().playSound(soundId);
		}
		catch(Exception e)
		{
            e.printStackTrace();
		}

		return null;
	}
}

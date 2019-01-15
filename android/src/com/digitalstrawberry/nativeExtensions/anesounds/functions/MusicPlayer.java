package com.digitalstrawberry.nativeExtensions.anesounds.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREWrongThreadException;

import com.digitalstrawberry.nativeExtensions.anesounds.ANESoundsContext;

public abstract class MusicPlayer implements FREFunction
{
	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{
		ANESoundsContext soundsContext = (ANESoundsContext) context;

		try
		{
			return this.action(soundsContext, args);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

    public abstract FREObject action(ANESoundsContext context, FREObject[] args) throws FRETypeMismatchException, FREInvalidObjectException, FREWrongThreadException;
}

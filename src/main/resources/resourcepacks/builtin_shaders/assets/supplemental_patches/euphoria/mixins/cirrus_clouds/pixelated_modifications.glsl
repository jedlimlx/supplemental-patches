#ifdef CIRRUS_CLOUDS
	vec3 pos = PixelateUnboundCloudPos((cloudAltitude != cloudAlt1i ? tracePosM * vec3(1,5,1) : tracePosM) - windOffset, pixelStepM);
#else
	vec3 pos = PixelateUnboundCloudPos(tracePosM - windOffset, pixelStepM);
#endif

materialMask = 0.0;
color.rgb = sqrt3(color.rgb);
color.rgb *= 0.7;
if (dither > 0.4) discard;

#ifdef NO_RAIN_ABOVE_CLOUDS
    if (cameraPosition.y > maximumCloudsHeight) discard;
#endif
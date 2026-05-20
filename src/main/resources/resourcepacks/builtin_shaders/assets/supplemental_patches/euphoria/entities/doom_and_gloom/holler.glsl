#if MC_VERSION < 12101
	// for some reason the holler in 1.21.1 is cooked
	emission = 2.0 * HOLLER_GLOWING_INTENSITY;
	color.rgb *= color.rgb;
#endif

fogOverride = 1.0;

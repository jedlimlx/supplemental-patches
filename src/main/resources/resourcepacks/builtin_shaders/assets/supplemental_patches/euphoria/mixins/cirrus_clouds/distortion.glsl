#if defined DOUBLE_UNBOUND_CLOUDS && defined CIRRUS_CLOUDS
	if (cloudAltitude != cloudAlt1i) {  // checking for 2nd layer
		#if PIXELATED_UNBOUND_CLOUDS > 0
			// apply a simpler stretching because fbm doesn't play nice with the pixelation function
			pos *= vec3(1, 1, 3);
		#else
			#if CLOUD_QUALITY_INTERNAL >= 2
				totalDistortion += fbm3d_3d(CIRRUS_DISTORTION_SCALE * tracePosM, 3);
			#else
				totalDistortion.xz += fbm2d_2d(CIRRUS_DISTORTION_SCALE * tracePosM.xz, 3);
			#endif
			pos += totalDistortion * CIRRUS_DISTORTION_INTENSITY * 0.05;
		#endif
	}
#endif

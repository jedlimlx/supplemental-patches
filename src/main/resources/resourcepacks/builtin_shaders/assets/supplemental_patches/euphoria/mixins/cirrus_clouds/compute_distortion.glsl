vec3 totalDistortion = vec3(0.0);
#if defined DOUBLE_UNBOUND_CLOUDS && defined CIRRUS_CLOUDS
    if (cloudAltitude != cloudAlt1i) {  // checking for 2nd layer
        #if CLOUD_QUALITY_INTERNAL >= 2
            totalDistortion = fbm3d_3d(CIRRUS_DISTORTION_SCALE * tracePosM, 3);
        #else
            totalDistortion.xz = fbm2d_2d(CIRRUS_DISTORTION_SCALE * tracePosM.xz, 3);
        #endif
        totalDistortion *= CIRRUS_DISTORTION_INTENSITY * 0.05;
    }
#endif
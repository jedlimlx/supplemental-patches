subsurfaceMode = 1, isFoliage = true;

smoothnessG = color.r * 0.5;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

#ifdef GLOWING_WART
    if (color.r > 0.6) { // Nether Wart Block
        overlayNoiseEmission = 0.28;
        emission = 16.0 * color.g;
        #ifdef GBUFFERS_TERRAIN
            vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.5)
                      + floor(playerPos.y + cameraPosition.y + 0.5);
            bpos = bpos * 0.01 + 0.005 * frameTimeCounter;
            emission *= pow2(texture2D(noisetex, bpos).r * pow1_5(texture2D(noisetex, bpos * 0.5).r));
            emission *= 4.0;
        #endif
    }
#endif
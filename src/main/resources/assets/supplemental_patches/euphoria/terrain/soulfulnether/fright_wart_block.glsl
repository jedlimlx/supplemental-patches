smoothnessG = color.b;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

#ifdef GLOWING_WART
if (color.b > 0.24) {
    overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.8;
    emission = 2.4;
    #ifdef GBUFFERS_TERRAIN
        vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.5)
                  + floor(playerPos.y + cameraPosition.y + 0.5);
        bpos = bpos * 0.01 + 0.005 * frameTimeCounter;
        emission *= pow2(texture2D(noisetex, bpos).r * pow1_5(texture2D(noisetex, bpos * 0.5).r));
        emission *= 4.0;
    #endif

    color.gb *= 1.5;
}
#endif
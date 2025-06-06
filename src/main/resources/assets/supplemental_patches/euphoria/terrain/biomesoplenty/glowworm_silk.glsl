emission = 2.0 * pow2(pow2(pow2(color.b))) + color.r;

if ((mat % 4 == 0 && color.r > 0.5) || (mat % 4 == 2 && color.r > 0.24)) {
    overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.8;
    #ifdef GBUFFERS_TERRAIN
        vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.5)
                  + floor(playerPos.y + cameraPosition.y + 0.5);
        bpos = bpos * 0.001 + 0.001 * frameTimeCounter;
        emission *= pow2(texture2D(noisetex, bpos).r * pow1_5(texture2D(noisetex, bpos * 0.5).r));
        emission *= 10.0;
    #endif
}

sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;
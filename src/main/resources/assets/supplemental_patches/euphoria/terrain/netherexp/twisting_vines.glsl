noSmoothLighting = true;

#if defined COATED_TEXTURES && defined GBUFFERS_TERRAIN
    doTileRandomisation = false;
#endif

if (color.r > color.g) {
    emission = 1.5 * color.b;
} else if (color.r > 0.4) {
    emission = pow2(color.r);
    maRecolor = vec3(0.1);

    #ifdef GBUFFERS_TERRAIN
        vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.501)
                  + floor(playerPos.y + cameraPosition.y + 0.501);
        bpos = bpos * 0.01 + 0.004 * frameTimeCounter;
        emission *= texture2D(noisetex, bpos).r * pow1_5(texture2D(noisetex, bpos * 0.5).r);
        emission *= 16.0;
    #endif
}

isFoliage = false;
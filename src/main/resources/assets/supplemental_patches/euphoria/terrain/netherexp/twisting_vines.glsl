noSmoothLighting = true;

#if defined COATED_TEXTURES && defined GBUFFERS_TERRAIN
    doTileRandomisation = false;
#endif

if (color.r > color.g) {
    emission = 1.5 * color.b;
} else if (color.r > 0.4) {
    emission = 3.0 * color.r;
    maRecolor = vec3(0.1);
}

isFoliage = false;
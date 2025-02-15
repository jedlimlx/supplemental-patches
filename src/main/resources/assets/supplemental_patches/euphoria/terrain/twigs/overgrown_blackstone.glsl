if (color.r - color.b > 0.2 || color.b - color.r > 0.2) {
    #if defined COATED_TEXTURES && defined GBUFFERS_TERRAIN
        doTileRandomisation = false;
    #endif

    if (color.r > 0.91) {
        emission = 3.0 * color.g;
        color.r *= 1.2;
        maRecolor = vec3(0.1);
    }

    isFoliage = false;
} else {
    #include "/lib/materials/specificMaterials/terrain/blackstone.glsl"
}

noSmoothLighting = true;

if (
    CheckForColor(color.rgb, vec3(140, 161, 163)) ||
    CheckForColor(color.rgb, vec3(116, 137, 145)) ||
    CheckForColor(color.rgb, vec3(98, 119, 112)) ||
    CheckForColor(color.rgb, vec3(180, 189, 189))
) {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
    if (mat % 2 == 0) lmCoordM.x *= 0.88;
} else if (mat % 2 == 0) {
    vec3 originalColor = color.rgb;
    emission = 1.2 * max(color.r, max(color.g, color.b));
    color.rgb *= pow(originalColor, vec3(0.7));

    #ifdef GBUFFERS_TERRAIN
        if (abs(NdotU) < 0.1) {
            lmCoordM.x += 0.2 * (signMidCoordPos.y + 0.5);
        }
    #endif
}

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;
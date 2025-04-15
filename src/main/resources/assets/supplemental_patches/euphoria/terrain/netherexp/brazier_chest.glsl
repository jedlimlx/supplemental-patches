if (color.r + color.b + color.g > 2.25) {  // bone part
    smoothnessG = color.r * 0.2;
    smoothnessD = smoothnessG;

    #ifdef GBUFFERS_TERRAIN
        DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
    #endif

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
} else if (mat % 4 == 0 && (color.r > 3.0 * color.b || (color.r > 0.9 && abs(color.g - color.b) < 0.1))) {
    noDirectionalShading = true;
    emission = 5.00;
    color.rgb *= color.rgb;

    overlayNoiseIntensity = 0.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/soulSlate.glsl"
}

#ifdef GBUFFERS_TERRAIN
    if (mat % 4 == 0 && NdotU > 0.9) {
        lmCoordM.x *= 1.0 + 0.3 * smoothstep1(max0(1.0 - length(signMidCoordPos)));
    }
#endif
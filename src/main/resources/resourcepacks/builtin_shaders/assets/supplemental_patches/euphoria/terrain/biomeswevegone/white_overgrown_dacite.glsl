if (mat % 8 == 0) {  // Grass
    if (glColor.b < 0.999) { // Grass Block:Normal:Grass Part
        smoothnessG = pow2(pow2(color.g)) * 0.5;
        smoothnessD = smoothnessG;

        #ifdef GBUFFERS_TERRAIN
            DoBrightBlockTweaks(color.rgb, 0.75, shadowMult, highlightMult);
        #endif
    } else {
        #include "/lib/materials/specificMaterials/terrain/dirt.glsl"
    }
} else if (mat % 8 == 2) {  // Snow
    float dotColor = dot(color.rgb, color.rgb);
    if (dotColor > 1.5) { // Snowy Variants:Snowy Part
        #include "/lib/materials/specificMaterials/terrain/snow.glsl"

        overlayNoiseIntensity = 0.0;
    } else { // Snowy Variants:Dacite Part
        smoothnessG = pow2(pow2(color.g)) * 0.5;
        smoothnessD = smoothnessG;

        #ifdef GBUFFERS_TERRAIN
            DoBrightBlockTweaks(color.rgb, 0.75, shadowMult, highlightMult);
        #endif
    }
} else if (mat % 8 == 4) {
    vec3 fractPos = fract(playerPos + cameraPosition);
    if (NdotU > 0.9 || fractPos.y > 0.575) {
        #include "/lib/materials/specificMaterials/terrain/dirt.glsl"
    } else {
        smoothnessG = pow2(pow2(color.g)) * 0.5;
        smoothnessD = smoothnessG;

        #ifdef GBUFFERS_TERRAIN
            DoBrightBlockTweaks(color.rgb, 0.75, shadowMult, highlightMult);
        #endif
    }
}
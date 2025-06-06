if (color.r - color.b > 0.1) {
    emission = 6.0 * dot(color.rgb, color.rgb);
} else {
    smoothnessG = color.r * 0.35;
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
    noiseFactor = 0.77;
    #endif
}

if (mat % 4 == 0) {
    #ifdef GBUFFERS_TERRAIN
        vec3 fractPos = fract(playerPos.xyz + cameraPosition.xyz) - vec3(0.5);
        if (NdotU > 0.9) lmCoordM.x += 0.4 * smoothstep1(max0(1.0 - 2.0 * length(fractPos)));
    #endif
}
#ifdef GBUFFERS_TERRAIN
    vec3 fractPos = fract(playerPos.xyz + cameraPosition.xyz);
    if (fractPos.y < 0.5 && abs(NdotU) < 0.1) {  // side of skull
        if (color.r > 0.98 || color.r > 1.7 * color.g) {
            emission = 4.0;
            color.rgb *= sqrt1(GetLuminance(color.rgb));
        }
    } else if (fractPos.y > 0.55) {  // candle
        color.rgb *= 1.0 + 0.7 * pow2(max(0.6 - signMidCoordPos.y, float(NdotU > 0.9) * 1.6));
    }
#endif

smoothnessG = color.r * 0.2;
smoothnessD = smoothnessG;

#ifdef GBUFFERS_TERRAIN
    DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
#endif
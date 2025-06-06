smoothnessG = 1.6 * smoothstep1(min1(1.3 * max0(color.r - 0.18)));
smoothnessD = smoothnessG;

emission = color.b > color.r ? pow1_5(max0(2.0 * color.b - color.r)) + 0.1 : 0.0;

#if defined COATED_TEXTURES && !defined GBUFFERS_WATER
    noiseFactor = 0.66;
#endif

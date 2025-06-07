smoothnessG = max0(1.4 * pow2(color.b) - GetMaxColorDif(color.rgb));
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

if (bloomed) {
    emission = 1.5 * pow2(2.5 * max0(color.r - 0.6));
    if (color.r < 0.6) lmCoordM.x *= 0.9;
    else color.rgb = pow1_5(color.rgb);
}
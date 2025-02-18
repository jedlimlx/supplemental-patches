smoothnessG = pow2(pow2(color.g)) * 1.8;
smoothnessG = min1(smoothnessG);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

if (mat % 4 == 2) {
    lmCoordM.x *= 0.88;
    if (color.b > color.r + 0.18) {
        emission = 1.5;
        color.rgb *= color.rgb;
    }
}
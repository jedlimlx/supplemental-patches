highlightMult = 2.0;
smoothnessG = pow2(color.r) * 0.6;
smoothnessG = min1(smoothnessG);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif

if (mat % 4 == 2) {
    if (CheckForColor(color.rgb, vec3(235, 135, 255)) || CheckForColor(color.rgb, vec3(255, 199, 235))) {
        emission = 4.0;
    } else lmCoordM.x *= 0.80;
}


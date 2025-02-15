highlightMult = 2.0;
smoothnessG = 1.5 * pow3(color.b) + 0.5 * color.b;
smoothnessD = smoothnessG;

mossNoiseIntensity = 0.3, sandNoiseIntensity = 0.3;

if (mat % 4 == 2) {
    if (color.r > color.b) {
        emission = 5.5 * pow2(color.r);
        color.rgb *= pow1_5(color.rgb);
    } else lmCoordM.x *= 0.90;
}

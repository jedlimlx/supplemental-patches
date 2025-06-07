if (mat % 4 == 2) lmCoordM.x -= 0.15;

highlightMult = 2.0;
smoothnessG = pow3(color.g) + 0.2 * color.g;
smoothnessD = smoothnessG;

mossNoiseIntensity = 0.3, sandNoiseIntensity = 0.3;

if (color.r > color.g) {
    emission = 2.5;
}

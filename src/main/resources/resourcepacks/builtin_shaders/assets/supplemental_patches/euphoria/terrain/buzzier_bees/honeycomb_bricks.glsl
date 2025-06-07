smoothnessG = 0.5 * pow3(color.r);
smoothnessD = smoothnessG;

if (mat % 4 == 2) {
    smoothnessG *= 1.3 * sqrt1(color.r);
    smoothnessD = smoothnessG;
    highlightMult = 2.5;
}
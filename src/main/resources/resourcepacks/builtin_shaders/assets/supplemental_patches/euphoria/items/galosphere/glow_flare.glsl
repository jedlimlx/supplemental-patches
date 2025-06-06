smoothnessG = 1.0;
smoothnessD = smoothnessG;
if (color.b / color.r > 1.7)
    emission = pow2(color.b) * 1.6 + 0.2;
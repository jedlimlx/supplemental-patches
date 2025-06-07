smoothnessG = 1.0;
smoothnessD = smoothnessG;
color.rgb *= pow2(color.rgb);
emission = 1.0 / (0.7 + pow3(color.r - 1.0) + pow3(color.b) + pow3(color.g)) +
           1.0 / (0.7 + pow3(color.g - 1.0) + pow3(color.b) + pow3(color.r)) +
           1.0 / (0.7 + pow3(color.b - 1.0) + pow3(color.r) + pow3(color.g));
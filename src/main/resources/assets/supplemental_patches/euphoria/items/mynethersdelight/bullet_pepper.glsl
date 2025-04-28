smoothnessG = max0(1.2 * pow2(color.b) - GetMaxColorDif(color.rgb));
smoothnessD = smoothnessG;

emission = 1.5 * pow2(2.5 * max0(color.r - 0.6));
color.rgb = pow1_5(color.rgb);
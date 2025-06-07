smoothnessG = 0.5;
smoothnessD = smoothnessG;
highlightMult = 2.5;

emission = dot(color.rgb, color.rgb) * 0.3;
color.rgb *= color.rgb;
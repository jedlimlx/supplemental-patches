smoothnessG = 0.7;
smoothnessD = smoothnessG;

emission = 2.0 * pow2(pow2(max(color.r, color.b)));
color.rgb *= pow(color.rgb, vec3(0.5 * emission));
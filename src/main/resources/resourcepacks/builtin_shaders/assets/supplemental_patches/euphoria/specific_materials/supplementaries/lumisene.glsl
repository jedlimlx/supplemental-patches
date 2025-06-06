color.a = 0.5 + 0.1 * fresnel;

emission = 0.6 * dot(color.rgb, color.rgb);
color.rgb = saturateColors(color.rgb, 1.8 - color.a);

smoothnessG = 1.2 - 0.3 * emission;

translucentMultCalculated = true;
reflectMult = 1.2 * smoothnessG;
translucentMult.rgb = pow2(color.rgb) * 0.4;

highlightMult = 2.5;
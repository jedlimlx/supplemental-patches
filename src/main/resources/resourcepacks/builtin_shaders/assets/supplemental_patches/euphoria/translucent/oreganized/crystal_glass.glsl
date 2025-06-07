smoothnessG = 1.0;
highlightMult = 3.5;
reflectMult = 0.5;

translucentMultCalculated = true;
translucentMult.rgb = smoothstep1(color.rgb);

color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);
color.a = CRYSTAL_GLASS_OPACITY;
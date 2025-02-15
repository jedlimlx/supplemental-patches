color.a = CRYSTAL_GLASS_OPACITY;

smoothnessG = 1.0;
highlightMult = 1.5 * smoothnessG;
reflectMult = 0.5;

color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

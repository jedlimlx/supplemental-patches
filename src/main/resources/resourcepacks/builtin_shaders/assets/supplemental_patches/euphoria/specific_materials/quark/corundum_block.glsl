float dotColor = dot(color.rgb, color.rgb);
translucentMultCalculated = true;
reflectMult = clamp(dotColor * 0.7 - factor * 0.2, 0.6, 0.0);
translucentMult.rgb = pow2(color.rgb) * 0.5;

highlightMult = factor * 3.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

#if GLOWING_AMETHYST >= 1
    emission = dotColor * 1.3 - factor * 0.5;
#endif

smoothnessG = 1.0 - factor * 0.5;
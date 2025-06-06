vec2 coord = floor(signMidCoordPos * 8.0) / 8.0;
emission = 0.6;
color.rgb *= pow2(pow2(color.rgb));

#ifdef ANIMATED_END_LAMP
    float factor = color.b > 0.94 ? 0.5 : 0.0;
    color.rgb *= ((1 - factor) + factor * Noise3D(vec3(0.5 * coord, frameTimeCounter * 0.008)));

    lmCoordM.x = max(lmCoordM.x, 1.0);
#endif

smoothnessG = 1.5 * factor;
smoothnessD = smoothnessG;
highlightMult = 1.0 + 1.5 * factor;

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
#endif
noSmoothLighting = true;

if (color.b - color.r < 0.1) {
    #include "/lib/materials/specificMaterials/terrain/lanternMetal.glsl"
}

if (color.g - color.r < 0.05) {
    emission = 4.3 * max0(color.r - color.b);
    emission += min(pow2(pow2(0.75 * dot(color.rgb, color.rgb))), 4.7);
    color.gb *= pow(vec2(0.8, 0.7), vec2(sqrt(emission)));
}

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(color, vec4(1.0, 0.6, 0.2, 1.0), emission, 5.0, lViewPos);
#endif

#ifdef GBUFFERS_TERRAIN
    if (abs(NdotU) < 0.1 && color.g > 0.6) {  // lantern side
        float factor = max0(1.4 - length(signMidCoordPos - vec2(0.0, 1.0)));
        lmCoordM.x += smoothstep1(0.4 * factor);
    } else if (NdotU < -0.9 && color.g > 0.8) {  // lantern base
        float factor = max0(1.6 - length(signMidCoordPos));
        lmCoordM.x += smoothstep1(factor);
    }
#endif
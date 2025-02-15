if (color.r > 0.95) {
    noSmoothLighting = true;
    lmCoordM.x = 0.77;

    emission = 4.3 * (color.g + color.b) * 0.5;
    emission += min(pow2(pow2(0.75 * dot(color.rgb, color.rgb))), 5.0);
    color.gb *= pow(vec2(0.8, 0.7), vec2(sqrt(emission) * 0.5));

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(1.0, 0.6, 0.2, 1.0), emission, 5.0, lViewPos);
    #endif
} else {
    #include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"
}
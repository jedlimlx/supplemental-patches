if (color.r > 0.7) {
    lmCoordM = vec2(1.0, 0.0);

    float dotColor = dot(color.rgb, color.rgb);
    emission = min(pow2(pow2(pow2(dotColor * 0.6))), 6.0) * 0.8 + 0.5;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.5, lViewPos);
    #endif

    overlayNoiseIntensity = 0.3;
} else {
    lmCoordM.x *= 0.85;
    #include "/lib/materials/specificMaterials/terrain/stone.glsl"
}
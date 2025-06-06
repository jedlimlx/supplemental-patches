if (color.g < 0.5) {
    lmCoordM = vec2(1.0, 0.0);

    float dotColor = dot(color.rgb, color.rgb) / 3;
    color.rgb /= dotColor;
    emission = sqrt(color.b + 8.0);

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.5, lViewPos);
    #endif
} else {
    lmCoordM.x *= 0.85;
    #include "/lib/materials/specificMaterials/terrain/endStone.glsl"
}
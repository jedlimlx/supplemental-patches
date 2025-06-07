if (color.r > 0.85) {
    noSmoothLighting = true;
    lmCoordM.x = 1.0;
    emission = GetLuminance(color.rgb) * 3.2;

    color.r *= 1.4;
    color.b *= 0.5;
    overlayNoiseIntensity = 0.0;

    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        if (color.g > 0.999) color.rgb = changeColorFunction(color.rgb, 1.5, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        if (color.g > 0.5) color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
    #endif
} else {
    lmCoordM.x -= 0.13;
    #include "/lib/materials/specificMaterials/terrain/stone.glsl"
}

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(color, vec4(1.0, 0.6, 0.2, 1.0), emission, 5.0, lViewPos);
#endif
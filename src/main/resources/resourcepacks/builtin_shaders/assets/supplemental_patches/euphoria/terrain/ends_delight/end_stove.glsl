if (color.r > color.b * 2) {
    float dotColor = dot(color.rgb, color.rgb);

    emission = 2.5 * dotColor * max0(pow2(pow2(pow2(color.r))) - color.b) + pow(dotColor * 0.35, 32.0);
    color.r *= 1.0 + 0.1 * emission;

    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
    #endif
    overlayNoiseIntensity = 0.0;
} else if (color.r > color.b) {
    #include "/lib/materials/specificMaterials/terrain/endStone.glsl"
} else {
    highlightMult = 2.0;
    smoothnessG = pow2(color.r) * 0.6;
    smoothnessG = min1(smoothnessG);
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.5;
    #endif
}
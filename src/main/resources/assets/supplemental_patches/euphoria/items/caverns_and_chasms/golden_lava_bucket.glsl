if (
    CheckForColor(color.rgb, vec3(117, 40, 2)) ||
    CheckForColor(color.rgb, vec3(204, 134, 3)) ||
    CheckForColor(color.rgb, vec3(168, 91, 23)) ||
    CheckForColor(color.rgb, vec3(250, 214, 74)) ||
    CheckForColor(color.rgb, vec3(255, 220, 80)) ||
    CheckForColor(color.rgb, vec3(225, 169, 13)) ||
    CheckForColor(color.rgb, vec3(255, 250, 152)) ||
    CheckForColor(color.rgb, vec3(255, 255, 255))
) {
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
} else {
    emission = color.r + color.g - color.b * 1.5;
    emission *= 1.8;
    color.rg += color.b * vec2(0.4, 0.15);
    color.b *= 0.8;
    if (LAVA_TEMPERATURE != 0.0) maRecolor += LAVA_TEMPERATURE * 0.1;
    emission *= LAVA_EMISSION;
    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
    #endif
}
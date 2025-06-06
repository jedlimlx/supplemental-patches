lmCoordM = vec2(0.9, 0.0);
emission = min(color.r, 0.7) * 1.4;

float dotColor = dot(color.rgb, color.rgb);
if (CheckForColor(color.rgb, vec3(221, 54, 72)) || CheckForColor(color.rgb, vec3(137, 29, 52))) {
   emission = 4.0;
} else if (abs(dotColor - 1.5) > 1.4) {
    emission = 5.0;
} else {
    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
    #endif
}
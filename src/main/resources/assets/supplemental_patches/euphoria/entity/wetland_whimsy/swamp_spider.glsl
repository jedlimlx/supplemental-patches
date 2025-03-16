if (color.r > 2 * color.g) {
    #if GLOWING_BLOODCAP_MUSHROOM == 1
        emission = color.r;
    #elif GLOWING_BLOODCAP_MUSHROOM == 2
        emission = 1.5 * color.r;
    #elif GLOWING_BLOODCAP_MUSHROOM == 3
        emission = 2.0 * color.r;
    #endif
}
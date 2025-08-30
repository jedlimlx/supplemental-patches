#ifndef NOT_GLOWING_CHORUS_FLOWER
    if (abs(color.r - color.b) < 0.05) {
        emission = max0(color.b * 2.0 - color.r) * 1.5;
    }
#endif
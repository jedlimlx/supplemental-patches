#ifndef NOT_GLOWING_CHORUS_FLOWER
    emission = min(GetLuminance(color.rgb), 0.75) / 0.75;
    emission = pow2(pow2(emission));
    lmCoordM.x = 1.0;
#else
    emission = 0.0;
    lmCoordM.x = 0.0;
#endif
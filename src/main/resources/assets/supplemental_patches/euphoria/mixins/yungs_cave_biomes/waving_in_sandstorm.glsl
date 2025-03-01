#ifdef MOD_YUNGSCAVEBIOMES
    wave *= mix(clamp(lmCoord.y - 0.87, 0.0, 0.1), SANDSTORM_WAVING_INTENSITY, sandstormFactor);
#else
    wave *= clamp(lmCoord.y - 0.87, 0.0, 0.1);
#endif
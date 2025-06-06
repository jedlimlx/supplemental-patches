#ifdef MOD_YUNGSCAVEBIOMES
    wave *= mix(clamp(lmCoord.y - 0.87, 0.0, 0.1), YUNGS_SANDSTORM_WAVING_INTENSITY, yungSandstormFactor);
#else
    wave *= clamp(lmCoord.y - 0.87, 0.0, 0.1);
#endif
#ifdef MOD_SCORCHFUL
    wave *= mix(1.0, SCORCHFUL_SANDSTORM_FOLIAGE_WAVING_INTENSITY, rainFactor * hasSandstorm);
    wave.x -= pow1_5(SCORCHFUL_SANDSTORM_LEAVES_WAVING_INTENSITY * (1 + 2 * wave.x)) * rainFactor * hasSandstorm;
#endif
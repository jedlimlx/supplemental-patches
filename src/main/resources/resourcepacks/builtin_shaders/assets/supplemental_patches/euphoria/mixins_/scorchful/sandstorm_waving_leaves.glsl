#ifdef MOD_SCORCHFUL
    wave.x -= pow1_5(SCORCHFUL_SANDSTORM_LEAVES_WAVING_INTENSITY * (0.3 + 2 * wave.x)) * rainFactor * hasSandstorm;
#endif
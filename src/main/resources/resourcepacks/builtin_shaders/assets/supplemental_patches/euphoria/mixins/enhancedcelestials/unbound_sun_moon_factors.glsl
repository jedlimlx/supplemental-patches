#ifdef MOD_ENHANCEDCELESTIALS
    sunSizeFactor1 = cos(sqrt(moonSizeSmooth / 20) * acos(sunSizeFactor1));
    sunSizeFactor2 /= sqrt(moonSizeSmooth / 20);
    moonPhaseFactor1 *= pow(moonSizeSmooth / 20, 0.2);
    moonPhaseFactor2 /= (moonSizeSmooth / 20);
#endif
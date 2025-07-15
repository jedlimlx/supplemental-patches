smoothnessG = pow(color.r, 16.0) * 1.8;
smoothnessD = smoothnessG;
highlightMult = 1.5;

#ifdef GBUFFERS_TERRAIN
    DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);

    DoOceanBlockTweaks(smoothnessD);
#endif

#if RAIN_PUDDLES >= 1 || defined SPOOKY_RAIN_PUDDLE_OVERRIDE
    noPuddles = 1.0;
#endif
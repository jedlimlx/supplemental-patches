if (color.r > 0.94) {
    emission = 1.3;
    color.rgb *= color.rgb;
}

#ifdef GBUFFERS_TERRAIN
    float temp = 0.4 + 0.3 * min1(pow2(max0(1.5 - length(signMidCoordPos - vec2(0.0, -0.5)))));
    lmCoordM.x = max(lmCoordM.x, min1(temp));
#endif
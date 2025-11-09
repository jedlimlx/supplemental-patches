#ifdef (SUBTLER_END_BEAMS == 1 && defined MOD_ENDERSCAPE) || SUBTLER_END_BEAMS == 2
    return beams.rgb * 0.6;
#else
    return beams.rgb;
#endif
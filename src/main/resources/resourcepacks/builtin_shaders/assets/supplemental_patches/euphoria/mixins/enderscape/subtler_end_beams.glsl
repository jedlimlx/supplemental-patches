#ifdef (SUBTLER_END_BEAMS == 1 && defined MOD_ENDERSCAPE) || SUBTLER_END_BEAMS == 2
    return beams * 0.1;
#else
    return beams;
#endif

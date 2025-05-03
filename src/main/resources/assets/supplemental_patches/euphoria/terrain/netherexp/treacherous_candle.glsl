if (mat % 4 == 0) {
    #ifdef GBUFFERS_TERRAIN
        lmCoordM.x += 0.8 * NdotU * max0(0.3 - length(absMidCoordPos));
    #endif
}
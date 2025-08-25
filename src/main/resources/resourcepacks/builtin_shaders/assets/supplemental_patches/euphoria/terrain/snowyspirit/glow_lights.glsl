float blockRes = absMidCoordPos.x * atlasSize.x;
vec2 signMidCoordPosM = (signMidCoordPos + 1) * 8;

if (
    maxAll(abs(signMidCoordPosM - vec2(4, 1))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(2, 7))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(0, 12))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(16, 12))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(11, 8))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(6, 10))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(8, 4))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(9, 14))) < 1 ||
    maxAll(abs(signMidCoordPosM - vec2(14, 3))) < 1
) {
    emission = 1.0;
    #ifdef GBUFFERS_TERRAIN  // TODO better animations for this
        vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.501)
                  + floor(playerPos.y + cameraPosition.y + 0.501);
        bpos = bpos * 0.0025 + 0.0015 * frameTimeCounter;
        emission *= texture2D(noisetex, bpos).r * pow1_5(texture2D(noisetex, bpos * 0.5).r);
        emission *= 16.0;
        emission = min(emission, 2.0);  // ensure it doesn't get too large
    #endif
}
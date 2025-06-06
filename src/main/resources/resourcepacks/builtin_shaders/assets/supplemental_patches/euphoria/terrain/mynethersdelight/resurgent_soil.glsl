if (mat % 4 == 2) lmCoordM.x *= 0.9;
smoothnessG = color.r * 0.4;
smoothnessD = color.r * 0.25;

float factor = color.b / color.r;
if (factor > 1.1) {
    #ifdef GBUFFERS_TERRAIN
        vec2 bpos = floor(playerPos.xz + cameraPosition.xz + 0.5)
                  + floor(playerPos.y + cameraPosition.y + 0.5);
        bpos = bpos * 0.05 + 0.004 * frameTimeCounter;
        emission = 12.0 * pow2(texture2D(noisetex, bpos).r * pow2(texture2D(noisetex, bpos * 0.5).r));
        emission *= factor;
        emission = min(emission, 5.0);
    #endif
}
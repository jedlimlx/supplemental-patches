noSmoothLighting = true;

float NdotE = dot(normalM, eastVec);
if (abs(abs(NdotE) - 0.5) < 0.4) {
    if (color.b - color.r > 0.35) {
        emission = 3.0 * pow2(color.b);
    } else {
        lmCoordM.x *= 0.88;
    }
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
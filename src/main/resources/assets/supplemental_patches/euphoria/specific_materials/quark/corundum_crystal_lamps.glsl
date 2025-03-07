float dotColor = dot(color.rgb, color.rgb);
translucentMultCalculated = true;
reflectMult = dotColor * 0.7 - factor * 0.2;
translucentMult.rgb = pow2(color.rgb) * 0.5;

smoothnessG = 1.2 - factor * 0.5;
highlightMult = factor * 3.0;

if (lit) {
    noSmoothLighting = true;
    lmCoordM.x *= 0.88;

    emission = pow2(min(color.r, min(color.g, color.b))) * 7.0;
    if (color.r < color.b && color.r < color.g) {
        color.r *= 1.0 - emission * 0.07;
    } else if (color.g < color.r && color.g < color.b) {
        color.g *= 1.0 - emission * 0.07;
    } else {
        color.b *= 1.0 - emission * 0.07;
    }

    emission *= 1.3;

    overlayNoiseIntensity = 0.5;
}

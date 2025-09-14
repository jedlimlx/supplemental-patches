subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;

if (color.r > color.b || color.r > 0.95 || color.b > 0.95) {
    emission = 3.0 * pow2(color.r);
    color.rgb *= color.rgb;
} else {
    lmCoordM.x *= 0.88;
}
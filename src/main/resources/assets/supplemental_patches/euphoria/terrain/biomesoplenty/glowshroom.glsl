if (color.b - color.r > 0.35) {
    emission = 3.0 * pow2(color.b);
} else {
    lmCoordM.x *= 0.88;
}
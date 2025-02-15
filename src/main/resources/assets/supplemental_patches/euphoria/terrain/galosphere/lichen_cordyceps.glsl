if (mat % 4 == 1) {
    emission = 0.1 * color.r;
} else {
    if (color.r > 0.8) {
        emission = 1.3 * pow2(color.r);
    } else lmCoordM.x -= 0.15;
}
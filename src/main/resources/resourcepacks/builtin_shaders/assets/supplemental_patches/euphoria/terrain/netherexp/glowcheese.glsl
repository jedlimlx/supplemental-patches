if (color.r > 0.77 && color.r < color.g * 2.0) {
    emission = pow2(color.r) + pow2(color.g);
}
if (
    color.g / color.r > 1.1 &&
    color.g / color.r < 1.3 &&
    color.b / color.r < 0.7
) {  // Experience Particle
    emission = 10.0 * pow2(color.g);
    color.rgb = pow1_5(color.rgb);
}
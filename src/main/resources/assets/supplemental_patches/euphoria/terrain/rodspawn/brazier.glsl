float factor = smoothstep1(min1(color.r * 1.5));
factor = factor > 0.12 ? factor : factor * 0.5;
smoothnessG = factor;
smoothnessD = factor;

if (mat % 4 == 0) {
    emission = color.g > 0.35 ? 2.5 * pow1_5(color.g) : 0.0;
} else {
    emission = color.b > 0.35 ? 2.5 * pow1_5(color.b) : 0.0;
}
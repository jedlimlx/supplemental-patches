color.r *= color.r * 1.05;
color.gb *= pow1_5(color.gb);

materialMask = OSIEBCA; // Intense Fresnel
smoothnessG = color.r * 0.5 + 0.2;
float factor = pow2(smoothnessG);
highlightMult = factor * 2.0 + 1.0;
smoothnessD = min1(factor * 2.0);

overlayNoiseIntensity = 0.3;

if (color.r > 0.27) {
    emission = 3.0 * pow1_5(color.r);
}
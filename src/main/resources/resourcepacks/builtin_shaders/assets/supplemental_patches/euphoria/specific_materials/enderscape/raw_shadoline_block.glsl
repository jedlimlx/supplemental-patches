materialMask = OSIEBCA; // Intense Fresnel
smoothnessG = pow2(pow2(color.g)) * 1.2 + 0.2 * color.b;
smoothnessD = smoothnessG;
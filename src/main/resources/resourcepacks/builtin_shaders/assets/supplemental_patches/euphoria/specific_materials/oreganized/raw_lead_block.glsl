materialMask = OSIEBCA; // Intense Fresnel
smoothnessG = pow2(pow2(max(color.r, color.b))) * 0.8 + 0.2 * color.b;
smoothnessD = smoothnessG;
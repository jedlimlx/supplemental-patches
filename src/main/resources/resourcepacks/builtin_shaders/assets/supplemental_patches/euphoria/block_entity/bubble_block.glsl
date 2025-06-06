materialMask = OSIEBCA; // Intense Fresnel

// TODO improve colour change effect on bubble blocks
float fresnel = clamp(1.0 + dot(normalM, normalize(viewPos)), 0.0, 1.0);
color.r = clamp(color.r * (1.0 - fresnel), 0.0, 1.0);
color.g = clamp(color.g * (1.0 + fresnel), 0.0, 1.0);
emission = fresnel;

smoothnessG = 1.0;
smoothnessD = smoothnessG;
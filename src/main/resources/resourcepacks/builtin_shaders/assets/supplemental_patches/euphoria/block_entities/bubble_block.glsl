materialMask = OSIEBCA; // Intense Fresnel

float fresnel = clamp(1.0 + dot(normalM, normalize(viewPos)), 0.0, 1.0);

float hue = fresnel;
vec3 hsvColor = rgb2hsv(color.rgb);
color.rgb = mix(color.rgb, hsv2rgb(vec3(hsvColor.r + hue, hsvColor.g * 2.0, hsvColor.b)), 0.6);

smoothnessG = 0.4;
smoothnessD = smoothnessG;

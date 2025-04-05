float maxComponent = max(max(color.r, color.g), color.b);
float minComponent = min(min(color.r, color.g), color.b);
float saturation = (maxComponent - minComponent) / (1.0 - abs(maxComponent + minComponent - 1.0));

emission = (2.5 * saturation + 0.3) * color.r;
emission *= mix(1.0, 0.1, min1(lViewPos / 96.0));  // fade-off into the distance
color.rgb *= pow(color.rgb, vec3(0.1 + 0.2 * emission) * vec3(1.4, 1.0, 1.0));

smoothnessG = 0.1;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif
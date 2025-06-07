float maxComponent = max(max(color.r, color.g), color.b);
float minComponent = min(min(color.r, color.g), color.b);
float saturation = (maxComponent - minComponent) / (1.0 - abs(maxComponent + minComponent - 1.0));

smoothnessG = max0(color.b - pow2(saturation) * 0.5) * 0.5 + 0.1;
smoothnessD = smoothnessG;

emission = saturation > 0.5 ? 2.5 : 0.0;
color.rgb = pow(color.rgb, vec3(1.0 + 0.75 * emission));
smoothnessG = dot(color.rgb, vec3(0.5));
smoothnessG = min1(smoothnessG);
smoothnessD = smoothnessG;

if (color.r > 0.7) {
	emission = 2.35;
	color.rgb *= sqrt1(GetLuminance(color.rgb));
} else emission = 0.1 + color.r;

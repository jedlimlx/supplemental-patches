noSmoothLighting = true; noDirectionalShading = true;
lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

float hue = rgb2hsv(color.rgb).r * 360;
if (NdotU > 0.85 || hue < 26 || hue > 37) {
	emission = 3.0;
	color.rgb = pow1_5(color.rgb);
	color.rgb = min1(color.rgb + 0.1 * normalize(color.rgb));

	overlayNoiseIntensity = 0.0;
} else if (abs(NdotU) < 0.5) {
	lmCoordM.x = min1(0.7 + 0.3 * smoothstep1(max0(0.4 - signMidCoordPos.y)));
} else {
	lmCoordM.x = 0.7;
}

emission += 0.0001; // No light reducing during noon

#ifdef DISTANT_LIGHT_BOKEH
	DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
#endif

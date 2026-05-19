noSmoothLighting = true;

vec3 worldPos = fract(playerPos.xyz + cameraPosition.xyz);
if (worldPos.y > 0.52) {
	color.rgb *= 1.0 + 0.7 * pow2(max(-signMidCoordPos.y + 0.6, float(NdotU > 0.9) * 1.6));

	#ifdef SNOWY_WORLD
	snowFactor = 0.0;
	#endif

	overlayNoiseIntensity = 0.3;
}

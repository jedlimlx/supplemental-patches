float GetVoidFogFactor() {
	float factor = max(
		exp((eyeAltitude - VOID_FOG_HIGHER_ALT)/VOID_FOG_FADE_RATE),
		exp((VOID_FOG_LOWER_ALT - eyeAltitude)/VOID_FOG_FADE_RATE)
	);
	return factor;
}

void DoVoidFog(inout vec4 color, float lViewPos) {
	#ifdef MOD_ENDERSCAPE
		float fog = lViewPos * min(GetVoidFogFactor(), 1.0);
		fog = 1.0 - exp(-fog);

		color.rgb = mix(color.rgb, vec3(0.0, 0.0, 0.0), fog);
	#endif
}

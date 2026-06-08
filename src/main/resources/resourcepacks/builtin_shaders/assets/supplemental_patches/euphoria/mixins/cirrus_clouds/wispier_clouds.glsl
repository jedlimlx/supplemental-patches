#if defined DOUBLE_UNBOUND_CLOUDS && defined CIRRUS_CLOUDS
	if (cloudAltitude != cloudAlt1i) noise *= mix(0.2, 1.2, pow2(Noise3D(vec3(tracePos.xz, (worldDay * 24000.0 + worldTime) / 24000.0))));
#endif

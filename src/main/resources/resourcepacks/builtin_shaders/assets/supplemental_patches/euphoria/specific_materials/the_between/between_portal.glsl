lmCoordM = vec2(0.0);

int sampleCount = 8;

float multiplier = 0.4 / (-viewVector.z * sampleCount);
vec2 interval = viewVector.xy * multiplier;
vec2 coord = signMidCoordPos * 0.5 + 0.5;
vec2 absMidCoordPos2 = absMidCoordPos * 2.0;
vec2 midCoord = texCoord - absMidCoordPos * signMidCoordPos;
vec2 minimumMidCoordPos = midCoord - absMidCoordPos;

for (int i = 0; i < sampleCount; i++) {
	float portalStep = (i + dither) / sampleCount;
	coord += interval * portalStep;

	vec2 sampleCoord = fract(coord) * absMidCoordPos2 + minimumMidCoordPos;
	vec4 psample = texture2DLod(tex, sampleCoord, 0);

	float factor = 1.0 - portalStep;
	psample *= pow(factor, 0.1);

	emission = max(emission, 5.0 * (1.0 - psample.r));
}

if (unstable) {
	color.rgb = saturateColors(color.rgb, 1.1);

	color.a = sqrt1(color.a) / 3.0;
	emission *= emission;

	float rand = hash11(frameTimeCounter);
	float flickerFraction = 0.95;
	if (rand > flickerFraction) {
		color.a *= 2.0 * (rand - flickerFraction) / (1.0 - flickerFraction);
	}
} else {
	color.rgb = saturateColors(color.rgb, 1.5);
	color.a = sqrt1(color.a) / 1.5;
}

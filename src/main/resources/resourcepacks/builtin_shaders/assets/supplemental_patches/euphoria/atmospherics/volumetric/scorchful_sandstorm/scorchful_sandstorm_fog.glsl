#include "/lib/colors/lightAndAmbientColors.glsl"

vec4 GetVolumetricSandstorm(vec3 color, vec3 translucentMult, vec3 nPlayerPos, vec3 playerPos, float lViewPos, float lViewPos1, float dither) {
    if (isEyeInWater != 0) return vec4(0.0);
    vec4 sandstorm = vec4(1.5 * mix(vec3(1.0, 0.7, 0.3), vec3(0.9, 0.8, 0.55), hasRegularSandstorm) * ambientColor, 0.0);

    float maxDist = renderDistance;

    #if VOLUMETRIC_SCORCHFUL_SANDSTORM == 2
    int sampleCount = int(maxDist / 8.0 + 0.001);

    vec3 traceAdd = nPlayerPos * maxDist / sampleCount;
    vec3 tracePos = cameraPosition;
    tracePos += traceAdd * dither;
    #else
    int sampleCount = int(maxDist / 16.0 + 0.001);

    vec3 traceAdd = 0.75 * nPlayerPos * maxDist / sampleCount;
    vec3 tracePos = cameraPosition;
    tracePos += traceAdd * dither;
    tracePos += traceAdd * sampleCount * 0.25;
    #endif

    vec3 translucentMultM = pow(translucentMult, vec3(1.0 / sampleCount));

    for (int i = 0; i < sampleCount; i++) {
        tracePos += traceAdd;

        vec3 tracedPlayerPos = tracePos - cameraPosition;
        float lTracePos = length(tracedPlayerPos);
        if (lTracePos > lViewPos1 / 2) break;

        vec3 wind = vec3(frameTimeCounter * -0.008, frameTimeCounter * -0.004, 0);

        vec3 tracePosM = tracePos * 0.001;
        tracePosM += Noise3D(tracePosM - wind) * 0.05;
        tracePosM = tracePosM * vec3(2.0, 0.5, 2.0);

        float traceAltitudeM = abs(tracePos.y - SCORCHFUL_SANDSTORM_LOWER_ALT);
        if (tracePos.y < SCORCHFUL_SANDSTORM_LOWER_ALT) traceAltitudeM *= 10.0;
        traceAltitudeM = 1.0 - min1(abs(traceAltitudeM) / SCORCHFUL_SANDSTORM_HEIGHT);

        float skylightCheck = pow2(min1(eyeBrightness.y / 240.0));
        for (int h = 0; h < 4; h++) {
            float noise1 = Noise3D((tracePosM + wind) * 0.002 * (h + 1));
            float stormSample = pow2(noise1);
            stormSample = pow2(pow2(stormSample));
            stormSample *= sqrt1(max0(1.0 - lTracePos / maxDist));
            stormSample *= traceAltitudeM * skylightCheck;

            sandstorm.a += stormSample;
            tracePosM *= 2.0;
            wind *= -2.0;
        }

        if (lTracePos > lViewPos) sandstorm.rgb *= translucentMultM;
    }

    sandstorm.a = clamp01(sandstorm.a * 1.5 - 0.2);
    sandstorm.rgb *= 1.0 - maxBlindnessDarkness;
    return sandstorm;
}
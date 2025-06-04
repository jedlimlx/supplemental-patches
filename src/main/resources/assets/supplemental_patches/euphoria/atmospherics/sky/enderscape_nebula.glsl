const float PI = 3.14159265359;

// thanks ChatGPT
vec3 snapToCubeSphereGrid(vec3 wpos, int N) {
    // Determine dominant axis (which cube face)
    vec3 absW = abs(wpos);
    float maxVal = max(max(absW.x, absW.y), absW.z);

    vec3 faceNormal;
    vec2 uv;

    // Identify the cube face and compute UV
    if (absW.x >= absW.y && absW.x >= absW.z) {
        faceNormal = vec3(sign(wpos.x), 0.0, 0.0);
        uv = vec2(wpos.y, wpos.z) / absW.x;
    } else if (absW.y >= absW.x && absW.y >= absW.z) {
        faceNormal = vec3(0.0, sign(wpos.y), 0.0);
        uv = vec2(wpos.x, wpos.z) / absW.y;
    } else {
        faceNormal = vec3(0.0, 0.0, sign(wpos.z));
        uv = vec2(wpos.x, wpos.y) / absW.z;
    }

    // Convert UV from [-1,1] → [0,1], snap to grid center, then back to [-1,1]
    uv = (uv + 1.0) * 0.5;                     // map to [0,1]
    uv = floor(uv * float(N)) + 0.5;           // snap to center
    uv /= float(N);                            // normalize
    uv = uv * 2.0 - 1.0;                       // back to [-1,1]

    // Reconstruct 3D point on cube face
    vec3 cubeVec;
    if (faceNormal.x != 0.0) {
        cubeVec = vec3(faceNormal.x, uv.x, uv.y);
    } else if (faceNormal.y != 0.0) {
        cubeVec = vec3(uv.x, faceNormal.y, uv.y);
    } else {
        cubeVec = vec3(uv.x, uv.y, faceNormal.z);
    }

    return normalize(cubeVec);
}

#define ES_NEBULA_COLOR_1 vec3(ES_NEBULA_COLOR_1_R, ES_NEBULA_COLOR_1_G, ES_NEBULA_COLOR_1_B)
#define ES_NEBULA_COLOR_2 vec3(ES_NEBULA_COLOR_2_R, ES_NEBULA_COLOR_2_G, ES_NEBULA_COLOR_2_B)

const float[8] NOISE_AMPS = {
    ES_NEBULA_NOISE_AMP_1,
    ES_NEBULA_NOISE_AMP_2,
    ES_NEBULA_NOISE_AMP_3,
    ES_NEBULA_NOISE_AMP_4,
    ES_NEBULA_NOISE_AMP_5,
    ES_NEBULA_NOISE_AMP_6,
    ES_NEBULA_NOISE_AMP_7,
    ES_NEBULA_NOISE_AMP_8
};

vec2 fbm3d_2d(vec3 x) {
    vec2 v = vec2(0.0);
    float a = 1.0;

    #ifdef ES_NEBULA_DISTORATION_QUALITY == 1
        int octaves = 3;
    #elif ES_NEBULA_DISTORTION_QUALITY == 2
        int octaves = 5;
    #else
        int octaves = 8;
    #endif

    for (int i = 0; i < octaves; i++) {
        v += a * vec2(Noise3D(x), Noise3D(3.0 * x - 200.0));
        x = x * 2.0;
        a *= 0.5;
    }

    return v;
}

vec3 GetEnderscapeNebula(vec3 viewPos, float VdotU) {
    // get world position and snap to square coordinates
    vec3 wpos = normalize((gbufferModelViewInverse * vec4(viewPos * 1000.0, 1.0)).xyz);
    if (ES_NEBULA_RESOLUTION != -1)
        wpos = snapToCubeSphereGrid(wpos, ES_NEBULA_RESOLUTION);

    // compute fbm distortion
    vec2 distortion = 0.1 * ES_NEBULA_DISTORTION_INTENSITY * sin(
        3.0 * ES_NEBULA_DISTORTION_SCALE_2 * fbm3d_2d(0.05 * ES_NEBULA_DISTORTION_SCALE * wpos.xyz)
    );
    vec3 basis1 = vec3(-wpos.y, wpos.x, wpos.z);
    vec3 basis2 = cross(wpos.xyz, basis1);
    vec3 distortedWpos = normalize(wpos.xyz + distortion.x * basis1 + distortion.y * basis2);

    // generate noise
    float noise = 0;
    float colourNoise = 0;
    for (int i = 1; i < 8; i++) {
        if (NOISE_AMPS[i - 1] < 1e-5) continue;
        float temp = NOISE_AMPS[i - 1] * pow(Noise3D(3.0 * distortedWpos / pow3(i)), ES_NEBULA_WISPINESS);
        noise += temp;
        colourNoise += temp * pow(i, -1);
    }

    // compute hash for graininess
    float hash = hash13(ES_NEBULA_RESOLUTION * wpos);

    // compute colour to apply to nebula
    #ifdef BIOME_COLORED_ES_NEBULA
        vec3 colour = vec3(smoothEnderscapeNebulaRed, smoothEnderscapeNebulaGreen, smoothEnderscapeNebulaBlue) / 255;
    #else
        vec3 colour = mix(
            ES_NEBULA_COLOR_1,
            ES_NEBULA_COLOR_2,
            pow2(cos(25.0 * colourNoise))
        ) / 255;
    #endif
    colour = saturateColors(colour, ES_NEBULA_SATURATION);

    // add granininess to nebula
    noise *= mix(1, hash, ES_NEBULA_GRAININESS);

    #ifdef BIOME_COLORED_ES_NEBULA
        float intensityFactor = 30.0 * smoothEnderscapeNebulaAlpha;
    #else
        float intensityFactor = 1.0;
    #endif
    vec4 nebulaTexture = vec4(vec3(noise) * colour, ES_NEBULA_INTENSITY * intensityFactor);

    #if defined ATM_COLOR_MULTS || defined SPOOKY
        nebulaTexture.rgb *= sqrtAtmColorMult; // C72380KD - Reduced atmColorMult impact on some things
    #endif

    return max(nebulaTexture.rgb * nebulaTexture.a, vec3(0.0));
}
vec2 num = floor(16.0 * (playerPos.xz + cameraPosition.xz) + 16.0 * (playerPos.y + cameraPosition.y));
vec2 noise = texture2D(noisetex, num / 16.0).rb;

smoothnessG = pow2(color.r / color.b) * 0.001 * (1 + 100.0 * max(0.5 * (sin(noise.r) + cos(noise.g)), 0));
smoothnessD = smoothnessG * 0.4;
highlightMult = 3.0;

#ifdef COATED_TEXTURES
    noiseFactor = 0.80;
#endif
vec2 num = (playerPos.xz + cameraPosition.xz) + (playerPos.y + cameraPosition.y);
vec2 noise = texture2D(noisetex, num / 128.0).rb;

smoothnessG = 0.5 * pow2(color.b) * (0.4 + (pow2(sin(noise.r) + cos(noise.g))));
smoothnessD = smoothnessG;
highlightMult = 3.0 * pow2(pow2(color.r)) * smoothnessG;

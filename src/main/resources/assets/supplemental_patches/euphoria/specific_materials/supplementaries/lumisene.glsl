vec2 num = floor(16.0 * (playerPos.xz + cameraPosition.xz) + 16.0 * (playerPos.y + cameraPosition.y));
vec2 noise = texture2D(noisetex, 0.4 * playerPos.xz + 0.4 * vec2(playerPos.y) + vec2(num)).rb;

emission = 50.0 * (1 - color.b) * (1 - color.r) * (1 - color.g);
emission *= mix(1.0, min1(max(noise.r, noise.g) * 1.7), pow2(7 * 0.1));
smoothnessG = 1.5;
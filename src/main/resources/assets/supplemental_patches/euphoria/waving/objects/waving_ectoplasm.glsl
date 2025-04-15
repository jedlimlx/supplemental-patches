if (fract(worldPos.y + 0.005) > 0.06) {
    float ectoplasmWaveTime = frameTimeCounter * 3.5 * WAVING_SPEED;
    worldPos.xz /= 8.0;

    float noise = 2 * (Noise3D(worldPos) - 0.5);
    float noise2 = Noise3D(worldPos + vec3(0.2, 1.0, 0.9));

    float wave = noise * sin(ectoplasmWaveTime * 0.5 + noise2 * 6.28);
    playerPos.y += wave * 0.1;
}
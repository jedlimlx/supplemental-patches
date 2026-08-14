if (fract(worldPos.y + 0.005) > 0.06) {
    float voidLachrymaWaveTime = frameTimeCounter * 3.5 * WAVING_SPEED;
    worldPos.xz *= 14.0;

    float wave = sin(voidLachrymaWaveTime * 0.7 + worldPos.x * 0.20 + worldPos.z * 0.10);
    wave += sin(voidLachrymaWaveTime * 0.5 + worldPos.x * 0.05 + worldPos.z * 0.10);
    wave += sin(voidLachrymaWaveTime * 0.1 + worldPos.x * 0.025 + worldPos.z * 0.05);

    #if VOID_LACHRYMA_WAVINESS >= 2
        wave *= 1.5;
    #elif VOID_LACHRYMA_WAVINESS >= 3
        wave *= 3.0;
    #endif

    playerPos.y += wave * 0.0125;
}

#if MOLTEN_LEAD_WAVINESS >= 1
if (fract(worldPos.y + 0.005) > 0.06) {
    float moltenLeadWaveTime = frameTimeCounter * 3.5 * WAVING_SPEED;
    worldPos.xz *= 14.0;

    float wave = sin(moltenLeadWaveTime * 0.7 + worldPos.x * 0.14 + worldPos.z * 0.07);
    wave += sin(moltenLeadWaveTime * 0.5 + worldPos.x * 0.05 + worldPos.z * 0.10);
    wave += sin(moltenLeadWaveTime * 0.1 + worldPos.x * 0.08 + worldPos.z * 0.04);

    #if MOLTEN_LEAD_WAVINESS >= 2
    wave *= 1.5;
    #elif MOLTEN_LEAD_WAVINESS >= 3
    wave *= 3.0;
    #endif

    playerPos.y += wave * 0.0125;
}
#endif
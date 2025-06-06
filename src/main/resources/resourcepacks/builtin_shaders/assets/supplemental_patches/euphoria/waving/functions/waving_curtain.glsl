void DoWave_Curtain(inout vec3 playerPos, vec3 worldPos, float waveMult, float angle) {
    worldPos.y *= 0.5;

    vec3 wave = GetWave(worldPos, 170.0);
    wave.x = wave.x * 10.0 * sin(angle) + wave.y * 4.0;
    wave.y = 0.0;
    wave.z = wave.z * 10.0 * cos(angle);

    playerPos.xyz += wave * waveMult;
}
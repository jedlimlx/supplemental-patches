void DoWave_Flesh(inout vec3 playerPos, vec3 worldPos, float waveMult) {
    worldPos *= 0.75;

    vec3 wave = GetWave(worldPos, 100.0 * FLESH_WAVING_SPEED);
    wave *= vec3(8.0, 16.0, 8.0);

    playerPos.xyz += wave * waveMult;
}
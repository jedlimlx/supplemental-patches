if (abs(color.b - color.g) < 0.07 && color.b > 0.35) { // Flat Part
    #include "/lib/materials/specificMaterials/planks/frightPlanks.glsl"

    #ifdef GLOWING_NETHER_TREES
} else { // Emissive Part
    emission = pow2(color.b) * 5.0 + 0.15;
    overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.3;
    #ifdef ANIMATED_FRIGHT_STEM
        #if defined GBUFFERS_TERRAIN
            vec3 wind = vec3(0, 0.0075, 0) * frameTimeCounter;
            float noise = Noise3D(0.01 * (playerPos.xyz + cameraPosition.xyz) + wind);
            emission *= 0.1 + 0.9 * pow2(noise);
            emission *= 4.0;
        #endif
    #endif
    #endif
}
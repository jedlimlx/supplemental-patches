if (color.g < 0.1) {  // Stem Part
    smoothnessG = 0.4;
    smoothnessD = smoothnessG;
#ifdef GLOWING_NETHER_TREES                                    
} else if (mat % 4 == 2 && (color.b < 0.62 || color.r / color.b < 0.81)) {
    emission = color.b * 4.5 - 0.2;
    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.15;
#endif
} else {  // Wood Part
    #include "/lib/materials/specificMaterials/planks/umbralPlanks.glsl"
}

if (mat % 4 == 3) {  // Powered Redstone Components
    redstoneIPBR(color.rgb, emission);
}
if (color.r > color.b * 3.0) { // Gilded Blackstone:Gilded Part
    #include "/lib/materials/specificMaterials/terrain/rawGoldBlock.glsl"
    #ifdef GLOWING_ORE_GILDEDBLACKSTONE
        emission = color.g * 1.5;
        emission *= GLOWING_ORE_MULT;

        overlayNoiseIntensity = 0.65, overlayNoiseEmission = 0.6;

        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
        #endif
    #endif
} else { // Gilded Blackstone:Blackstone Part
    #include "/lib/materials/specificMaterials/terrain/blackstone.glsl"
}
if (GetMaxColorDif(color.rgb) < 0.01) {
    #include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"
} else {
    float emissiveness = 5.0;
    #include "/lib/materials/specificMaterials/terrain/moltenLead.glsl"
}
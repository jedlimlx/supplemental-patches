if (color.r - color.b > 0.4 && color.r - color.g < 0.2) {  // gold coin on (Alolan) Meowth head
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"

    smoothnessG *= 2.0;
    smoothnessD *= 2.0;
}
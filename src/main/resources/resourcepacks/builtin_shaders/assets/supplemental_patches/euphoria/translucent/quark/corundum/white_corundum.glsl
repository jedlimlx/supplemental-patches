bool lit = mat % 2 == 0;
float factor = (color.r + color.b + color.g) / 2.3;

if (mat % 4 == 2) {
    #include "/lib/materials/specificMaterials/terrain/corundumBlock.glsl"
} else {
    #include "/lib/materials/specificMaterials/terrain/corundumCrystalLamps.glsl"
}
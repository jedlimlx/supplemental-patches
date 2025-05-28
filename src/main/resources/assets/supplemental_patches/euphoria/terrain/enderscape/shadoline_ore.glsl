if (color.g < 0.42 || CheckForColor(color.rgb, vec3(120, 146, 140))) {  // Raw Shadoline Part
    #include "/lib/materials/specificMaterials/terrain/rawShadolineBlock.glsl"
} else {  // Endstone / Mirestone Part
    if (mat % 4 == 0) {
        #include "/lib/materials/specificMaterials/terrain/endstone.glsl"
    } else {
        #include "/lib/materials/specificMaterials/terrain/mirestone.glsl"
    }
}
// TODO improve ender watcher shader
if (color.r > 0.5 && color.b < 0.3) {  // Redstone
    emission = 1.0;
    #include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"
} else if (color.b > 0.5) {  // Eye of Ender
    smoothnessG = 1.0;
    highlightMult = 2.0;
    smoothnessD = 1.0;
}
vec3 fractPos = abs(fract(playerPos.xyz + cameraPosition.xyz) - 0.5);
if (color.r > 0.5 && maxAll(fractPos.xz) > 0.43) {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
} else {
    emission = 2.0 * pow2(color.g);
}
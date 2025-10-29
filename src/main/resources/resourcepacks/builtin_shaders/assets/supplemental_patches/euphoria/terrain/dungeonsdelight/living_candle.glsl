vec3 fractPos = fract(playerPos.xyz + cameraPosition.xyz);
if (mat % 4 == 0 && fractPos.y > 0.5) {
    #include "/lib/materials/specificMaterials/terrain/candle.glsl"
}
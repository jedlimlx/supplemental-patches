noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.9333);

vec3 worldPos = playerPos + cameraPosition;
vec3 fractPos = fract(worldPos.xyz);
vec2 coordM = abs(fractPos.xz - 0.5);
if (max(coordM.x, coordM.y) < 0.375 && fractPos.y > 0.3 && NdotU > 0.9) {
    float emissiveness = 5.0;
    #include "/lib/materials/specificMaterials/terrain/moltenLead.glsl"

    sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/anvil.glsl"
}
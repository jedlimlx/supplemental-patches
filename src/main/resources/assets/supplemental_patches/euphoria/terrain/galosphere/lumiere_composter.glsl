vec3 worldPos = playerPos + cameraPosition;
vec3 fractPos = fract(worldPos.xyz);
vec2 coordM = abs(fractPos.xz - 0.5);
if (max(coordM.x, coordM.y) < 0.375 && NdotU > 0.9) {
    #ifdef GLOWING_LUMIERE >= 1
        emission = 1.3 * pow2(color.r) + 3.8 * pow2(pow2(color.r));
    #endif

    smoothnessG = 3.0;
    smoothnessD = smoothnessG;
} else {
    #include "/lib/materials/specificMaterials/planks/junglePlanks.glsl"
}
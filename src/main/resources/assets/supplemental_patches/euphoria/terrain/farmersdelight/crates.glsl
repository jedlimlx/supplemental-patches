vec2 absCoord = abs(signMidCoordPos);
if (
    (absCoord.x > 0.75 || absCoord.y > 0.75 && NdotU > 0.9) || (
        NdotU < 0.1 && NdotU > -0.1 && absCoord.x < 0.75 && (
            absCoord.y > 0.75 ||
            (signMidCoordPos.y < -0.25 && signMidCoordPos.y > -0.50) ||
            (signMidCoordPos.y > 0.0 && signMidCoordPos.y < 0.25) ||
            signMidCoordPos.y > 0.50
        )
    ) || NdotU < -0.9
) {
    #include "/lib/materials/specificMaterials/planks/darkOakPlanks.glsl"
    if (mat % 4 == 0) lmCoordM.x -= 0.1;
} else {
    if (mat % 4 == 0) {
        subsurfaceMode = 1;
        lmCoordM.x *= 0.875;

        if (color.r > 0.64) {
            emission = color.r < 0.75 ? 2.5: 8.0;
            color.rgb = color.rgb * vec3(1.0, 0.8, 0.6);
        }
    } else if (mat % 4 == 2) {
        #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
    }
}
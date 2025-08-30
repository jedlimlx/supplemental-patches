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
    #include "/lib/materials/specificMaterials/planks/oakPlanks.glsl"
} else {
    #ifndef NOT_GLOWING_CHORUS_FLOWER
        emission = max0(color.b * 2.0 - color.r) * 1.5;
    #endif
}
vec2 absCoord = abs(signMidCoordPos);
if (NdotU > 0.9 && (absCoord.x < 0.875 && absCoord.y < 0.875)) {
    subsurfaceMode = 1;
    lmCoordM.x *= 0.875;

    if (color.r > 0.64) {
        emission = color.r < 0.75 ? 1.0: 3.0;
        color.rgb = color.rgb * vec3(1.0, 0.8, 0.6);
    }
} else {
    lmCoordM.x -= 0.15;
    #include "/lib/materials/specificMaterials/terrain/sack.glsl"
}
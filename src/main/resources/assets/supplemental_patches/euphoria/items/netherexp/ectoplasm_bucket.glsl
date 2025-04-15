if (GetMaxColorDif(color.rgb) < 0.01) {
    #include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"
} else {
    lmCoordM = vec2(0.0);

    emission = smoothstep1(sqrt1(color.b)) + 0.2;
    emission *= 2.0;

    color.rgb *= pow(color.rgb, vec3(0.5 + 0.3 * emission));
}
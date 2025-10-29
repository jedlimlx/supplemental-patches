float dotColor = dot(color.rgb, color.rgb);
if (dotColor > 1.5) { // Snowy Variants:Snowy Part
    #include "/lib/materials/specificMaterials/terrain/snow.glsl"

    overlayNoiseIntensity = 0.0;
} else if (color.r / color.b > 2.5) {  // Gelisol
    smoothnessG = pow2(pow2(color.r));
    smoothnessD = smoothnessG;
}

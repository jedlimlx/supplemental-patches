noSmoothLighting = true;
#include "/lib/materials/specificMaterials/terrain/leadBlock.glsl"

if (mat % 2 == 0) {
    if (mat % 4 == 0) {
        smoothnessG *= 0.5;
        smoothnessD *= 0.5;

        emission = GetLuminance(color.rgb) * 3.0;
        emission += pow2(color.b) * 0.7 + 0.1;
    } else {
        smoothnessG = 0.0;
        smoothnessD = 0.0;

        float emissiveness = 6.0;
        #include "/lib/materials/specificMaterials/terrain/moltenLead.glsl"

        color.rgb *= 1.1 - 0.1 * GetLuminance(color.rgb);
    }
}
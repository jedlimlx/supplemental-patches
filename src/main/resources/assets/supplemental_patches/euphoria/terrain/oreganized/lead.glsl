#include "/lib/materials/specificMaterials/terrain/leadBlock.glsl"

if (mat % 4 == 2) {
    smoothnessG *= 0.5;
    smoothnessD *= 0.5;

    emission = GetLuminance(color.rgb) * 3.0;
    emission += pow2(color.b) * 0.7;
}

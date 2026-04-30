#include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"

if (entityId % 4 == 1) {
    smoothnessG *= 1.5;
    smoothnessD *= 1.5;
}
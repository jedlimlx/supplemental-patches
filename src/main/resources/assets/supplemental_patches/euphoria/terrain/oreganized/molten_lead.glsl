float emissiveness = 5.0;
#include "/lib/materials/specificMaterials/terrain/moltenLead.glsl"

#ifdef MOLTEN_LEAD_EDGE_EFFECT
    vec3 edgeColor = vec3(0.45, 0.5, 0.8) * 0.7;
    #include "/lib/materials/specificMaterials/terrain/fluidEdgeEffect.glsl"
#endif
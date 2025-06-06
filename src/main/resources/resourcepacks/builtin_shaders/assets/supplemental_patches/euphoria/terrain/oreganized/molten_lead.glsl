float emissiveness = 5.0;
#include "/lib/materials/specificMaterials/terrain/moltenLead.glsl"

#if MOLTEN_LEAD_EDGE_EFFECT > 0
    float easeAmount = 1.5;
    vec3 edgeColor = vec3(0.45, 0.5, 0.8) * 2.1;
    float edgeEmission = 1.0 + emission * 1.1;
    #if MOLTEN_LEAD_EDGE_EFFECT == 2
        edgeColor = vec3(0.18, 0.2, 0.3);
        edgeEmission = 0.0;
        easeAmount = 1.2;
    #endif

    #include "/lib/materials/specificMaterials/terrain/fluidEdgeEffect.glsl"
#endif
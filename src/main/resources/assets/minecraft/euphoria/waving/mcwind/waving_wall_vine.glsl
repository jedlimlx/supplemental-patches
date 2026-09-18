if (mcw_hasOccupancy()) {
    MCW_LOD_SKIP
    float weld = Weld_MCWIND(worldPos);
    vec3 delta = mcw_leafSway(worldPos, blockCenter, weld);
    delta.xz += mcw_vineSwing(worldPos, blockCenter, weld);

    #ifdef GBUFFERS_TERRAIN
    vec3 worldFaceNormal = normalize(mat3(gbufferModelViewInverse) * gl_NormalMatrix * gl_Normal);
    #else
    vec3 worldFaceNormal = normalize(mat3(shadowModelViewInverse) * gl_NormalMatrix * gl_Normal);
    #endif

    playerPos += mcw_vineMotion(delta, worldPos, blockCenter, worldFaceNormal);
    MCW_LOD_BLEND
} else return false;

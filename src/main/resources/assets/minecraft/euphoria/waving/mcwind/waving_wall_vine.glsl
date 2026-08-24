if (mcw_hasOccupancy()) {
    float weld = mcw_leafWeld(worldPos, blockCenter);
    vec3 delta = mcw_leafSway(worldPos, blockCenter, weld);
    delta.xz += mcw_vineSwing(worldPos, blockCenter, weld);

    #ifdef GBUFFERS_TERRAIN
        vec3 worldFaceNormal = normalize(mat3(gbufferModelViewInverse) * gl_NormalMatrix * gl_Normal);
    #else
        vec3 worldFaceNormal = normalize(mat3(shadowModelViewInverse) * gl_NormalMatrix * gl_Normal);
    #endif

    playerPos += mcw_vineMotion(delta, worldPos, blockCenter, worldFaceNormal);
} else return false;

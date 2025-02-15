vec3 fractPos = 2 * fract(worldPos) - 1;
float dist = max(max(abs(fractPos.x), abs(fractPos.y)), abs(fractPos.z));
if (dist > 0.875) {
    smoothnessG = 1.0;
    highlightMult = 3.5;
    smoothnessD = smoothnessG;
}
vec3 desaturate(vec3 color, float factor) {
    float greyscale = dot(color.rgb, vec3(0.3, 0.59, 0.11));
    return mix(color, vec3(greyscale), factor);
}
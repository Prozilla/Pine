vec4 snapToPixel(vec4 position) {
    vec2 normalizedPosition = position.xy / position.w;
    vec2 pixelPosition = (normalizedPosition * 0.5 + 0.5) * uViewportSize;
    pixelPosition = round(pixelPosition);
    normalizedPosition = (pixelPosition / uViewportSize) * 2 - 1;
    return vec4(normalizedPosition * position.w, position.z, position.w);
}
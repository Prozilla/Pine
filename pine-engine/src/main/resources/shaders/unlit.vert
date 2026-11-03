#version 410 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec4 vColor;
layout (location=2) in vec2 vTexCoords;
layout (location=3) in float vTexId;
layout (location=4) in float vTextureType;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform bool uSnapPixels;
uniform vec2 uViewportSize;

out vec4 fColor;
out vec2 fTexCoords;
flat out float fTexId;
flat out int fTextureType;

vec4 snapToPixel(vec4 position) {
    vec2 normalizedPosition = position.xy / position.w;
    vec2 pixelPosition = (normalizedPosition * 0.5 + 0.5) * uViewportSize;
    pixelPosition = round(pixelPosition);
    normalizedPosition = (pixelPosition / uViewportSize) * 2 - 1;
    return vec4(normalizedPosition * position.w, position.z, position.w);
}

void main() {
    // Pass properties to fragment shader
    fColor = vColor;
    fTexCoords = vTexCoords;
    fTexId = vTexId;
    fTextureType = int(round(vTextureType));;

    // Apply view and projection matrices
    mat4 mvp = uProjection * uView * uModel;
    vec4 position = mvp * vec4(vPosition, 1.0);
    bool isSignedDistanceField = (fTextureType & 2) != 0;
    if (uSnapPixels && !isSignedDistanceField) {
        position = snapToPixel(position);
    }
    gl_Position = position;
}

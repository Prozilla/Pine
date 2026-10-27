#version 410 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec3 vNormal;
layout (location=2) in vec4 vColor;
layout (location=3) in vec2 vTexCoords;
layout (location=4) in float vTexId;
layout (location=5) in float vIsArrayTexture;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform bool uSnapPixels;
uniform vec2 uViewportSize;

out vec3 fPosition;
out vec3 fNormal;
out vec4 fColor;
out vec2 fTexCoords;
flat out float fTexId;
flat out float fIsArrayTexture;
out vec3 fWorldPosition;

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
    fIsArrayTexture = vIsArrayTexture;

    // Calculate position
    mat4 modelView = uView * uModel;
    vec4 modelViewPosition =  modelView * vec4(vPosition, 1.0);
    vec4 position = uProjection * modelViewPosition;
    if (uSnapPixels) {
        position = snapToPixel(position);
    }
    gl_Position = position;
    fPosition = modelViewPosition.xyz;
    fWorldPosition = (uModel * vec4(vPosition, 1.0)).xyz;

    // Calculate normal
    // TODO: Support non-uniform scaling
    fNormal = (modelView * vec4(vNormal, 0.0)).xyz;
    if (length(fNormal) > 0) {
        // TODO: Normalize on CPU
        fNormal = normalize(fNormal);
    }
}

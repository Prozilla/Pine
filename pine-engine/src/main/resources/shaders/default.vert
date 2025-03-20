#version 330 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec4 vColor;
layout (location=2) in vec2 vTexCoords;
layout (location=3) in float vTexId;
layout (location=4) in float vIsArrayTexture;

uniform mat4 uView;
uniform mat4 uProjection;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out float fIsArrayTexture;

void main() {
    // Pass properties to fragment shader
    fColor = vColor;
    fTexCoords = vTexCoords;
    fTexId = vTexId;
    fIsArrayTexture = vIsArrayTexture;

    // Apply view and projection matrices
    gl_Position = uProjection * uView * vec4(vPosition, 1.0);
}

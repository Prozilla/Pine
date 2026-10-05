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

out vec3 fPosition;
out vec3 fNormal;
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

    // Calculate position and normal
    mat4 modelView = uView * uModel;
    vec4 modelViewPosition =  modelView * vec4(vPosition, 1.0);
    gl_Position = uProjection * modelViewPosition;
    fPosition = modelViewPosition.xyz;
    fNormal = normalize(modelView * vec4(vNormal, 0.0)).xyz;
}

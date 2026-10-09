#version 410 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec3 vNormal;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 fPosition;
out vec3 fNormal;

void main() {
    // Calculate position
    mat4 modelView = uView * uModel;
    vec4 modelViewPosition =  modelView * vec4(vPosition, 1.0);
    gl_Position = uProjection * modelViewPosition;
    fPosition = modelViewPosition.xyz;

    // Calculate normal
    fNormal = (modelView * vec4(vNormal, 0.0)).xyz;
    if (length(fNormal) > 0) {
        fNormal = normalize(fNormal);
    }
}

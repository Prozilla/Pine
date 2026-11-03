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

out vec4 gColor;
out vec2 gTexCoords;
flat out float gTexId;
flat out int gTextureType;

#include "common/snap-to-pixel.glsl"

void main() {
    gColor = vColor;
    gTexCoords = vTexCoords;
    gTexId = vTexId;
    gTextureType = int(round(vTextureType));

    vec4 position = uProjection * uView * uModel * vec4(vPosition, 1.0);
    if (uSnapPixels) {
        position = snapToPixel(position);
    }
    gl_Position = position;
}
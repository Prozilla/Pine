#version 410 core

in vec4 fColor;
in vec3 fTexCoords;

uniform samplerCube uCubemap;

uniform vec3 uFogColor;
uniform float uFogStart;
uniform float uFogEnd;

out vec4 color;

void main() {
    vec4 textureColor = fColor * texture(uCubemap, fTexCoords);
    float fogFactor = clamp((fTexCoords.y - uFogStart) / (uFogEnd - uFogStart), 0.0, 1.0);
    color = vec4(mix(uFogColor, textureColor.rgb, fogFactor), textureColor.a);
}

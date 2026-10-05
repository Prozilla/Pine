#version 410 core

in vec3 fBarycentric;
in vec4 fColor;

uniform float uLineWidth;
uniform vec4 uLineColor;
uniform float uFillAlpha;

out vec4 color;

void main() {
    vec3 width = fwidth(fBarycentric) * uLineWidth;
    vec3 edges = smoothstep(vec3(0.0), width, fBarycentric);
    float edge = min(min(edges.x, edges.y), edges.z);

    vec4 fill = vec4(fColor.rgb, fColor.a * uFillAlpha);
    color = mix(uLineColor, fill, edge);

    if (color.a <= 0.0) {
        discard;
    }
}
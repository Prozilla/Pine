#version 410 core

in vec3 fBarycentric;
in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in float fIsArrayTexture;

uniform sampler2D uTexture;
#if PLATFORM != MACOS
    uniform sampler2DArray uTextureArray;
#endif

uniform float uLineWidth;
uniform vec4 uLineColor;
uniform float uFillAlpha;

out vec4 color;

void main() {
    vec3 width = fwidth(fBarycentric) * uLineWidth;
    vec3 edges = smoothstep(vec3(0.0), width, fBarycentric);
    float edge = min(min(edges.x, edges.y), edges.z);

    vec4 fill = fColor;
    if (fTexId >= 0) {
        vec4 textureColor = vec4(1, 0, 1, 1); // Fallback color

        if (fIsArrayTexture > 0.5) {
            // Use texture array
            #if PLATFORM == MACOS
                textureColor = vec4(1, 0, 1, 1);
            #else
                textureColor = texture(uTextureArray, vec3(fTexCoords, round(fTexId)));
            #endif
        } else {
            // Use texture
            textureColor = texture(uTexture, fTexCoords);
        }

        fill *= textureColor;
    }

    fill.a *= uFillAlpha;
    color = mix(uLineColor, fill, edge);

    if (color.a <= 0.0) {
        discard;
    }
}
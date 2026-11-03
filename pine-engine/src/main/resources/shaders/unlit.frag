#version 410 core

const float SIGNED_DISTANCE_FIELD_EDGE = 128.0 / 255.0;
const float SIGNED_DISTANCE_FIELD_BIAS = 0.02;
const float MINIMUM_EDGE_SOFTNESS = 0.0001;

in vec4 fColor;
in vec2 fTexCoords;
flat in float fTexId;
flat in int fTextureType;

uniform sampler2D uTexture;
#if PLATFORM != MACOS
    uniform sampler2DArray uTextureArray;
#endif

out vec4 color;

vec4 decodeSignedDistanceField(vec4 texel) {
    float edgeDistance = texel.a;
    vec2 gradient = vec2(dFdx(edgeDistance), dFdy(edgeDistance));
    float softness = max(length(gradient) * 0.5, MINIMUM_EDGE_SOFTNESS);
    float edge = SIGNED_DISTANCE_FIELD_EDGE - SIGNED_DISTANCE_FIELD_BIAS;
    float coverage = smoothstep(edge - softness, edge + softness, edgeDistance);
    return vec4(texel.rgb, coverage);
}

void main() {
    if (fTexId >= 0) {
        vec4 textureColor = vec4(1, 0, 1, 1); // Fallback color

        if ((fTextureType & 1) != 0) {
            // Use texture array
            #if PLATFORM != MACOS
                textureColor = texture(uTextureArray, vec3(fTexCoords, round(fTexId)));
            #endif
        } else {
            // Use texture
            textureColor = texture(uTexture, fTexCoords);
        }

        if ((fTextureType & 2) != 0) {
            textureColor = decodeSignedDistanceField(textureColor);
        }

        color = fColor * textureColor;
    } else {
        color = fColor;
    }

    if (color.a <= 0) {
        discard;
    }
}

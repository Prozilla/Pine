#version 410 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in float fIsArrayTexture;

uniform sampler2D uTexture;

out vec4 color;

void main() {
    if (fTexId >= 0) {
        vec4 textureColor = vec4(1, 0, 1, 1); // Fallback color

        if (fIsArrayTexture > 0.5) {
            // Use texture array
            textureColor = vec4(1, 1, 1, 1);
        } else {
            // Use texture
            textureColor = texture(uTexture, fTexCoords);
        }

        color = fColor * textureColor;
    } else {
        color = fColor;
    }
}

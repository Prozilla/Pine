#version 410 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in float fIsArrayTexture;

uniform sampler2D uTexture;
uniform sampler2DArray uTextureArray;

out vec4 color;

void main() {
    if (fTexId >= 0) {
        vec4 textureColor = vec4(1, 0, 1, 1); // Fallback color

        if (fIsArrayTexture > 0.5) {
            // Use texture array
            textureColor = texture(uTextureArray, vec3(fTexCoords, int(fTexId)));
        } else {
            // Use texture
            textureColor = texture(uTexture, fTexCoords);
        }

        color = fColor * textureColor;
    } else {
        color = fColor;
    }
}

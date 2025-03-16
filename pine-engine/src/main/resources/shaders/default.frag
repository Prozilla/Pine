#version 330 core

#define MAX_TEXTURES 128

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[MAX_TEXTURES];

out vec4 color;

void main() {
    if (fTexId > 0) {
        vec4 textureColor = vec4(1, 0, 1, 1);

        int id = int(fTexId);
        textureColor = texture(uTextures[id], fTexCoords);

        color = fColor * textureColor;
    } else {
        color = fColor;
    }
}

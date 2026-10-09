#version 410 core

const float SPECULAR_POWER = 50;

in vec3 fPosition;
in vec3 fNormal;
in vec4 fColor;
in vec2 fTexCoords;
flat in float fTexId;
flat in float fIsArrayTexture;

uniform sampler2D uTexture;
#if PLATFORM != MACOS
    uniform sampler2DArray uTextureArray;
#endif

struct Surface {
    vec4 ambient;
    vec4 specular;
    float reflectance;
};
uniform Surface uSurface;

struct Sunlight {
    vec3 color;
    vec3 direction;
    float intensity;
};
uniform Sunlight uSunlight;

struct SkyLight {
    vec3 color;
    float intensity;
};
uniform SkyLight uSkyLight;

uniform mat4 uView;

out vec4 color;

vec3 computeLightColor(vec3 diffuse, vec3 specular, vec3 lightColor, float lightIntensity, vec3 position, vec3 lightDirection, vec3 normal) {
    // Diffuse
    float diffuseFactor = max(dot(normal, lightDirection), 0);
    vec3 diffuseColor = diffuse * lightColor * lightIntensity * diffuseFactor;

    // Specular
    vec3 specularColor = vec3(0);
    if (diffuseFactor > 0) {
        vec3 cameraDirection = normalize(-position);
        vec3 reflectedLight = reflect(-lightDirection, normal);
        float specularFactor = pow(max(dot(cameraDirection, reflectedLight), 0), SPECULAR_POWER);
        specularColor = specular * lightColor * lightIntensity * specularFactor * uSurface.reflectance;
    }

    return diffuseColor + specularColor;
}

vec3 computeSunlight(vec3 diffuse, vec3 specular, Sunlight light, vec3 position, vec3 normal) {
    vec3 lightDirection = normalize(mat3(uView) * light.direction);
    return computeLightColor(diffuse, specular, light.color, light.intensity, position, lightDirection, normal);
}

vec3 computeSkyLight(SkyLight light, vec3 ambient, vec3 diffuse) {
    return light.intensity * light.color * ambient * diffuse;
}

void main() {
    if (!gl_FrontFacing) {
        discard;
    }

    vec4 base = fColor;
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

        base = fColor * textureColor;
    }

    vec3 normal = fNormal;

    vec3 ambient = computeSkyLight(uSkyLight, uSurface.ambient.rgb, base.rgb);
    vec3 lit = ambient + computeSunlight(base.rgb, uSurface.specular.rgb, uSunlight, fPosition, normal);

    if (base.a <= 0.0) {
        discard;
    }

    color = vec4(lit, base.a);
}
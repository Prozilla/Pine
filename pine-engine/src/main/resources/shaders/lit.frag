#version 410 core

const float SPECULAR_POWER = 50;

in vec3 fPosition;
in vec3 fNormal;
in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in float fIsArrayTexture;

uniform sampler2D uTexture;
#if PLATFORM != MACOS
    uniform sampler2DArray uTextureArray;
#endif

struct Surface {
    vec4 ambient;
    vec4 diffuse;
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

struct SkyLight
{
    vec3 color;
    float intensity;
};
uniform SkyLight uSkyLight;

uniform mat4 uView;

out vec4 color;

vec4 computeLightColor(vec4 diffuse, vec4 specular, vec3 lightColor, float lightIntensity, vec3 position, vec3 lightDirection, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 1);
    vec4 specularColor = vec4(0, 0, 0, 1);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, lightDirection), 0.0);
    diffuseColor = diffuse * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

    // Specular Light
    vec3 cameraDirection = normalize(-position);
    vec3 reflectedLight = normalize(reflect(-lightDirection, normal));
    float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);
    specularColor = specular * lightIntensity * specularFactor * uSurface.reflectance * vec4(lightColor, 1.0);

    return diffuseColor + specularColor;
}

vec4 computeSunlight(vec4 diffuse, vec4 specular, Sunlight light, vec3 position, vec3 normal) {
    vec3 lightDirection = normalize(mat3(uView) * uSunlight.direction);
    return computeLightColor(diffuse, specular, light.color, light.intensity, position, lightDirection, normal);
}

vec4 computeSkyLight(SkyLight light, vec4 ambient) {
    return vec4(light.intensity * light.color, 1) * ambient;
}

void main() {
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

        color = fColor * textureColor;
    } else {
        color = fColor;
    }

    vec4 ambient = computeSkyLight(uSkyLight, color + uSurface.ambient);
    vec4 diffuse = color + uSurface.diffuse;
    vec4 specular = color + uSurface.specular;

    color = ambient + computeSunlight(diffuse, specular, uSunlight, fPosition, fNormal);
}

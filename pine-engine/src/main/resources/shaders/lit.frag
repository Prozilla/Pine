#version 410 core

const float SPECULAR_POWER = 50;
const int NUM_CASCADES = 3; // Must match CascadeShadow.COUNT
const float SHADOW_BIAS = 0.0005;
const bool DEBUG_SHADOWS = false;

in vec3 fPosition;
in vec3 fNormal;
in vec4 fColor;
in vec2 fTexCoords;
flat in float fTexId;
flat in float fIsArrayTexture;
in vec3 fWorldPosition;

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

struct Cascade {
    mat4 projView;
    float splitDistance;
};
uniform Cascade uCascades[NUM_CASCADES];
uniform sampler2D uShadowMaps[NUM_CASCADES];
uniform bool uShadowsEnabled;

struct Fog {
    vec3 color;
    float density;
};
uniform Fog uFog;

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

int computeCascadeIndex(float viewZ) {
    int cascade = 0;
    for (int i = 0; i < NUM_CASCADES - 1; i++) {
        if (viewZ < uCascades[i].splitDistance) {
            cascade = i + 1;
        }
    }
    return cascade;
}

float sampleShadowMap(int cascade, vec2 coords) {
    if (cascade == 0) return texture(uShadowMaps[0], coords).r;
    if (cascade == 1) return texture(uShadowMaps[1], coords).r;
    return texture(uShadowMaps[2], coords).r;
}

float computeShadow(int cascade, vec3 worldPosition) {
    if (!uShadowsEnabled) {
        return 1.0;
    }

    vec4 lightPosition = uCascades[cascade].projView * vec4(worldPosition, 1.0);
    vec3 coords = lightPosition.xyz / lightPosition.w * 0.5 + 0.5;
    if (coords.z > 1.0 || coords.x < 0.0 || coords.x > 1.0 || coords.y < 0.0 || coords.y > 1.0) {
        return 1.0;
    }

    float closestDepth = sampleShadowMap(cascade, coords.xy);
    return coords.z - SHADOW_BIAS > closestDepth ? 0.0 : 1.0;
}

vec3 desaturate(vec3 color, float factor) {
    float greyscale = dot(color.rgb, vec3(0.3, 0.59, 0.11));
    return mix(color, vec3(greyscale), factor);
}

vec3 computeFog(vec3 position, vec3 color, Fog fog, vec3 ambientLight, Sunlight sunlight) {
    vec3 fogColor = fog.color * (ambientLight + sunlight.color * sunlight.intensity);
    float distance = length(position);
    float fogFactor = 1.0 / exp((distance * fog.density) * (distance * fog.density));
    fogFactor = clamp(fogFactor, 0.0, 1.0);

    float desaturation = 1 - (fogFactor + 3.0) / 4.0;
    vec3 desaturatedColor = desaturate(color.rgb, desaturation);

    return mix(fogColor, desaturatedColor, fogFactor);
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

    if (base.a <= 0.0) {
        discard;
    }

    vec3 normal = fNormal;
    if (length(normal) > 0) {
        normal = normalize(normal);
    }

    int cascade = computeCascadeIndex(fPosition.z);
    float shadow = computeShadow(cascade, fWorldPosition);

    vec3 ambient = computeSkyLight(uSkyLight, uSurface.ambient.rgb, base.rgb);
    vec3 lit = ambient + computeSunlight(base.rgb, uSurface.specular.rgb, uSunlight, fPosition, normal) * shadow;
    vec3 foggy = computeFog(fPosition, lit, uFog, uSurface.ambient.rgb, uSunlight);

    if (DEBUG_SHADOWS && uShadowsEnabled) {
        vec3 tint = cascade == 0 ? vec3(1.0, 0.25, 0.25) : (cascade == 1 ? vec3(0.25, 1.0, 0.25) : vec3(0.25, 0.25, 1.0));
        lit *= tint;
    }

    color = vec4(foggy, base.a);
}
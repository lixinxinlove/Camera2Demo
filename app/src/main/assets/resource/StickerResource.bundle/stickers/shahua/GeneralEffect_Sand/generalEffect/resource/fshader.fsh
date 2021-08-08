precision highp float;

uniform sampler2D inputTexture;
uniform sampler2D noiseTexture;
uniform sampler2D filterTexture;

invariant varying vec2 imageUV;

vec4 doFilter(vec4 inputColor)
{
    vec4 textureColor = clamp(inputColor, 0.0, 1.0);

    float blueColor = textureColor.b * 63.0;

    vec2 quad1;
    quad1.y = floor(floor(blueColor) / 8.0);
    quad1.x = floor(blueColor) - (quad1.y * 8.0);
    vec2 quad2;
    quad2.y = floor(ceil(blueColor) / 8.0);
    quad2.x = ceil(blueColor) - (quad2.y * 8.0);

    vec2 texPos1;
    texPos1.x = (quad1.x * 0.125) + 0.5 / 512.0 + ((0.125 - 1.0 / 512.0) * textureColor.r);
    texPos1.y = (quad1.y * 0.125) + 0.5 / 512.0 + ((0.125 - 1.0 / 512.0) * textureColor.g);
    vec2 texPos2;
    texPos2.x = (quad2.x * 0.125) + 0.5 / 512.0 + ((0.125 - 1.0 / 512.0) * textureColor.r);
    texPos2.y = (quad2.y * 0.125) + 0.5 / 512.0 + ((0.125 - 1.0 / 512.0) * textureColor.g);
    
    vec4 newColor1 = texture2D(filterTexture, texPos1);
    vec4 newColor2 = texture2D(filterTexture, texPos2);

    vec4 color = mix(newColor1, newColor2, fract(blueColor));
    return color;
}

vec4 doNoise(vec4 inputColor)
{
    vec4 color = inputColor;
    vec4 noise1Color = texture2D(noiseTexture, imageUV/2.0);
    vec4 noise2Color = texture2D(noiseTexture, imageUV/2.0 + vec2(0.0, 0.5) - vec2(0.0 / 720.0, 1.0 / 1280.0));
    vec4 noise3Color = texture2D(noiseTexture, imageUV/2.0 + vec2(0.5, 0.0) - vec2(1.0 / 720.0, 0.0 / 1280.0));
    vec4 noise4Color = texture2D(noiseTexture, imageUV/2.0 + vec2(0.5, 0.5) - vec2(1.0 / 720.0, 1.0 / 1280.0));
    // color = color + noise1Color * vec4(200.0/255.0, 140.0/255.0, 30.0/255.0, 1.0) * 0.1;
    // color = color + noise2Color * vec4(200.0/255.0, 140.0/255.0, 30.0/255.0, 1.0) * 0.1;
    // color = color + noise3Color * vec4(200.0/255.0, 140.0/255.0, 30.0/255.0, 1.0) * 0.1;
    // color = color + noise4Color * vec4(255.0/255.0, 176.0/255.0, 49.0/255.0, 1.0) * 0.1;
    // float intensity = 0.08;
    // color = color + noise1Color * vec4(212.0/255.0, 160.0/255.0, 33.0/255.0, 1.0) * intensity;
    // color = color + noise2Color * vec4(212.0/255.0, 160.0/255.0, 33.0/255.0, 1.0) * intensity;
    // color = color + noise3Color * vec4(235.0/255.0, 175.0/255.0, 55.0/255.0, 1.0) * intensity;
    // color = color + noise4Color * vec4(220.0/255.0, 170.0/255.0, 44.0/255.0, 1.0) * intensity;

    color = color + noise1Color * vec4(219.0/255.0, 189.0/255.0, 142.0/255.0, 1.0) * 0.1;
    color = color + noise2Color * vec4(219.0/255.0, 189.0/255.0, 142.0/255.0, 1.0) * 0.1;
    color = color + noise3Color * vec4(219.0/255.0, 189.0/255.0, 142.0/255.0, 1.0) * 0.1;
    color = color + noise4Color * vec4(219.0/255.0, 189.0/255.0, 142.0/255.0, 1.0) * 0.1;

    return color;
}

void main() {

    vec4 color = texture2D(inputTexture, imageUV);
    color = doFilter(color);
    color = doNoise(color);
    gl_FragColor = color;
}

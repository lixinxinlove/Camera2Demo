precision highp float;

varying vec2 textureCoordinate;
varying vec2 textureCoordinateColor;

uniform sampler2D inputImageTexture;
uniform sampler2D mattingTexture;
uniform float isBigHead;

void main(){
    vec4 orig = texture2D(inputImageTexture, textureCoordinateColor);
    vec4 mask = texture2D(mattingTexture, textureCoordinate);
    gl_FragColor = vec4(mask.a * orig.xyz, mask.a);
}
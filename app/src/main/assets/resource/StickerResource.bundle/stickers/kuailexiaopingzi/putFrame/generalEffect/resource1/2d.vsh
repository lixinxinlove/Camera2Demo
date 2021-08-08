attribute vec3 attPosition;
attribute vec2 attUV;

varying vec2 textureCoordinate;

void main() {
    gl_Position = vec4(attPosition * 0.9 + vec3(0.012, 0.16,0.0) , 1.0);
    textureCoordinate = attUV.xy;
}

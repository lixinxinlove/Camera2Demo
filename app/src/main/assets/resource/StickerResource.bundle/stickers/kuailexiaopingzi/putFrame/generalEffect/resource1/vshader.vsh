attribute vec3 attPosition;
attribute vec2 attUV;

varying vec2 textureCoordinate;

void main() {
  vec2 pos = attPosition.xy;
  pos = pos * 1.0/3.0 + vec2(2.0/3.0, 2.0/3.0);
    gl_Position = vec4(pos, 0.0 , 1.0);
    textureCoordinate = attUV.xy;
}

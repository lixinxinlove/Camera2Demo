precision highp float;

varying vec2 textureCoordinate;

uniform sampler2D u_grab8;
void main() {
  gl_FragColor = texture2D(u_grab8, textureCoordinate);
}

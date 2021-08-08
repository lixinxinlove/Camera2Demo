precision highp float;

varying vec2 textureCoordinate;
uniform sampler2D inputImage;

void main() {
  vec2 coord = textureCoordinate;
  float row = floor(textureCoordinate.y*3.0);
  float col = floor(textureCoordinate.x*3.0); 
  vec2 center = 1.0/3.0 * vec2(col, row) + vec2(1.0/3.0);
  coord = (coord-center)*1.01+center;
  gl_FragColor = texture2D(inputImage, coord);
}

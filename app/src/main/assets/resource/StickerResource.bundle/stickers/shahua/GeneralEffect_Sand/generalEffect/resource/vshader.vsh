attribute vec3 attPosition;
attribute vec2 attUV;

invariant varying vec2 imageUV;

void main() {

    gl_Position = vec4(attPosition, 1.0);
    imageUV = attUV;
}

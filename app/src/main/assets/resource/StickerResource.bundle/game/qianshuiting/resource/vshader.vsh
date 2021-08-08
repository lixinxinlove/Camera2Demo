precision highp float;
 
attribute vec3 attPosition;
attribute vec2 attUV;
varying highp vec2 textureCoordinate;
 
uniform vec2 u_scale;
uniform float u_angle;
uniform vec2 u_position;
uniform int u_screenWidth;
uniform int u_screenHeight;
 
void main() {
    vec2 position = attPosition.xy;
    vec2 ratio_inv = vec2(u_screenWidth, u_screenHeight);
    vec2 ratio = vec2(1.0 / float(u_screenWidth), 1.0 / float(u_screenHeight));
    mat2 rotation = mat2(cos(u_angle), - sin(u_angle), sin(u_angle), cos(u_angle));
    position = position * ratio * float(u_screenWidth)* ratio_inv * u_scale * rotation * ratio + u_position;
    gl_Position = vec4(position.xy, attPosition.z , 1.0);
    textureCoordinate = attUV.xy;
}
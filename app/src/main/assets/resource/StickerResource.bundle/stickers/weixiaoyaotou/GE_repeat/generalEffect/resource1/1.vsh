precision highp float;

attribute vec3 attPosition;
attribute vec2 attUV;

varying vec2 textureCoordinate;
varying vec2 textureCoordinateColor;

uniform mat3 mattingMatrix;
uniform vec2 mattingScale;
uniform int m_displayWidth;
uniform int m_displayHeight;
uniform float bigHeadScale;
uniform float jiao;
uniform float uTime;

const float SHAKE_PERIOD = 2.0;

vec3 ResizeVec(vec3 pt, vec2 mpt, float k){
    vec3 outpt=vec3(0.0, 0.0, 0.0);
    outpt.x = mpt.x + k * (pt.x - mpt.x);
    outpt.y = mpt.y + k * (pt.y - mpt.y);
    return outpt;
}

vec3 RollVec(vec3 pt, vec2 mpt, float j){
    vec3 outpt=vec3(0.0, 0.0, 0.0);
    mat3 m=mat3(cos(j), 0.0-sin(j), 0.0,
                0.0+sin(j), cos(j), 0.0,
                0.0, 0.0, 1.0);
    outpt = m * vec3(pt.xy-mpt,1.0);
    outpt.xy = outpt.xy + mpt; 
    return outpt;
}

float GetShakeAmplitude(float fTime)
{
    float ret = mod(fTime, SHAKE_PERIOD) - SHAKE_PERIOD * 0.5;
    return 0.05 * ret;
}

void main() {
    float img_w = float(m_displayWidth) / mattingScale.x;
    float img_h = float(m_displayHeight) / mattingScale.y;
    vec3 src_pos = vec3((attPosition.xy+vec2(1.0,1.0))/2.0*vec2(128.0,128.0),1.0);
    vec3 dst_pos = vec3(0.0,0.0,0.0);
    dst_pos = mattingMatrix * src_pos;

    vec3 v_t0 = vec3(0.0,0.0,1.0);
    vec3 v_p0 = mattingMatrix*v_t0;
        
    vec3 v_t1 = vec3(128.0, 128.0, 1.0);
    vec3 v_p1 = mattingMatrix*v_t1;
        
    vec3 v_t2 = vec3(0.0, 128.0, 1.0);
    vec3 v_p2 = mattingMatrix*v_t2;
        
    vec3 v_t3 = vec3(128.0, 0.0, 1.0);
    vec3 v_p3 = mattingMatrix*v_t3;
        
        //计算两条直线的交点,从而把多边形放大
    float a =v_p1.y-v_p0.y;
    float b =v_p1.x*v_p0.y - v_p0.x*v_p1.y;
    float c =v_p1.x-v_p0.x;
    float d =v_p3.y-v_p2.y;
    float e =v_p3.x*v_p2.y - v_p2.x*v_p3.y;
    float f =v_p3.x-v_p2.x;
    float mx ,my;
    my = ( a*e-b*d )/(a*f-c*d);
    mx = ( my*c-b)/a;
    vec2 v_center_pt = vec2(mx,my);

    // dst_pos = ResizeVec(dst_pos, v_center_pt, 0.9);
    vec3 outpt = ResizeVec(dst_pos, v_center_pt, bigHeadScale);
    outpt = RollVec(outpt, v_center_pt, jiao);

    gl_Position = vec4(outpt.x/img_w*2.0-1.0, outpt.y/img_h*2.0-1.0, 1.0, 1.0);
    textureCoordinate = attUV;
    textureCoordinateColor = dst_pos.xy/vec2(img_w,img_h);
   
}

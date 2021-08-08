precision highp float;

uniform sampler2D u_historyTexture;
uniform vec2 u_current_position;
uniform vec2 u_last_position;
uniform int u_init;
uniform int u_screenWidth; 
uniform int u_screenHeight; 
uniform float u_speed;
uniform float u_fade_speed;
varying highp vec2 textureCoordinate;

#define kFade 0.0022 //越小停留时间越长

bool pointInRaius(vec2 center, vec2 pos, float radius)
{
    vec2 distance = center-pos;
    return step(radius, sqrt(distance.x*distance.x+distance.y*distance.y)) < 1.0;
}

float getCross(vec2 p1, vec2 p2, vec2 p)
{
    return (p2.x - p1.x) * (p.y - p1.y) -(p.x - p1.x) * (p2.y - p1.y);
}

bool pointInRect(vec2 rect_a, vec2 rect_b, vec2 p, float radius)
{
    vec2 rect_tan = rect_b - rect_a;
    if(rect_b.x == rect_a.x && rect_b.y == rect_a.y) return false;
    vec2  p1 = rect_a + radius * normalize(vec2(rect_tan.y, -rect_tan.x));
    vec2  p2 = rect_a - radius * normalize(vec2(rect_tan.y, -rect_tan.x));
    vec2  p3 = rect_b - radius * normalize(vec2(rect_tan.y, -rect_tan.x));
    vec2  p4 = rect_b + radius * normalize(vec2(rect_tan.y, -rect_tan.x));
    return (getCross(p1,p2,p) * getCross(p3,p4,p)>= 0.0 && getCross(p2,p3,p) * getCross(p4,p1,p) >= 0.0);
}

float intensity(vec2 rect_a, vec2 rect_b, vec2 p, float radius)
{
    float A = rect_b.y - rect_a.y;
    float B = rect_a.x - rect_b.x;
    float C = rect_b.x * rect_a.y - rect_a.x*rect_b.y;
    float d = abs(A*p.x + B*p.y+C)/distance(rect_a, rect_b)/radius;
    return mix(1.0, 0.0, d*d);
}

void main() {
    
    if(u_init > 0)
    {
        gl_FragColor = vec4(0.0,0.0,0.0,1.0);
    }else
    {
        float radius = u_speed*float(u_screenWidth);
        vec2 pointPos = vec2(textureCoordinate.x * float(u_screenWidth), textureCoordinate.y * float(u_screenHeight));
        vec2 currentPos = vec2(u_current_position.x * float(u_screenWidth), u_current_position.y * float(u_screenHeight));
        vec2 lastPos = vec2(u_last_position.x * float(u_screenWidth), u_last_position.y * float(u_screenHeight));
        float currentColor = 0.0;
        if (pointInRaius(currentPos, pointPos, radius)||pointInRect(lastPos, currentPos, pointPos, radius))
        {
            currentColor = 1.0;
        }
        float originColor = clamp(texture2D(u_historyTexture, textureCoordinate).r, 0.0, 1.0);
        originColor = step(0.5, originColor);
        float color = max(currentColor, originColor);
        gl_FragColor = vec4(vec3(float(color)),1.0);
    }
}
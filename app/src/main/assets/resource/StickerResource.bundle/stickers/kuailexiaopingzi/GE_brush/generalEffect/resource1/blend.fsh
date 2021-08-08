precision highp float;

varying vec2 textureCoordinate;
uniform sampler2D u_brushTexture;
uniform sampler2D u_grabOrigin;
uniform sampler2D u_paintTexture;
uniform int u_brush;

void main() {
    vec4 origin = texture2D(u_grabOrigin, textureCoordinate);
    vec4 paintColor = texture2D(u_paintTexture, textureCoordinate);
    if (u_brush == 0)
    {
        gl_FragColor = vec4(mix(origin.rgb, paintColor.rgb, paintColor.a),1.0);
    }else{
        float brush = texture2D(u_brushTexture, textureCoordinate).r;
        vec4 paintColor = texture2D(u_paintTexture, textureCoordinate);
        //vec4 color = vec4(mix(origin.rgb, paintColor.rgb, brush * brush), 1.0);
        gl_FragColor = vec4(mix(origin.rgb, paintColor.rgb, (1.0-brush * brush) * paintColor.a),1.0);
        //gl_FragColor = texture2D(u_brushTexture, textureCoordinate);
    }
}

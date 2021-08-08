precision highp float;

varying vec2 textureCoordinate;

uniform sampler2D u_grab0;
uniform sampler2D u_grab1;
uniform sampler2D u_grab2;
uniform sampler2D u_grab3;
uniform sampler2D u_grab4;
uniform sampler2D u_grab5;
uniform sampler2D u_grab6;
uniform sampler2D u_grab7;
uniform sampler2D u_grab8;
void main() {
        float row = floor(textureCoordinate.y*3.0);
        float col = floor(textureCoordinate.x*3.0); 
        vec2 coord = fract(textureCoordinate*3.0);
        if (row < 0.5 && col < 0.5){
            gl_FragColor = texture2D(u_grab0, coord);
          //gl_FragColor = vec4(1.0,0.0,0.0,1.0);
        }else if(row < 0.5 && col < 1.5){
            gl_FragColor = texture2D(u_grab1, coord);
        }else if(row < 0.5 && col < 2.5){
            gl_FragColor = texture2D(u_grab2, coord);
        }else if (row < 1.5 && col < 0.5){
            gl_FragColor = texture2D(u_grab3, coord);
        }else if(row < 1.5 && col < 1.5){
            gl_FragColor = texture2D(u_grab4, coord);
        }else if(row < 1.5 && col < 2.5){
            gl_FragColor = texture2D(u_grab5, coord);
        }else if (row < 2.5 && col < 0.5){
            gl_FragColor = texture2D(u_grab6, coord);
        }else if(row < 2.5 && col < 1.5){
            gl_FragColor = texture2D(u_grab7, coord);
        }else{
          //gl_FragColor = vec4(1.0,0.0,0.0,1.0);
           gl_FragColor = texture2D(u_grab8, coord);
        }
}

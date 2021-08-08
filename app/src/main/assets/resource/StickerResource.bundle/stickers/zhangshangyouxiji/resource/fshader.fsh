precision highp float;
precision highp int;
 
uniform sampler2D inputImageTexture;
uniform int u_data;
uniform int u_row;
uniform int u_col;
uniform int u_digitNum;

varying highp vec2 textureCoordinate;
 
vec2 getTextureCoord_rowfirst(int pos, vec2 base_coord, int r_num, int c_num) {   
    int unit_y = pos / c_num;
    int unit_x = pos - unit_y * c_num;
    vec2 coord = vec2( (base_coord.x + float(unit_x)) / float(c_num), (base_coord.y + float(unit_y)) / float(r_num));
    return coord;
}
 
void main() {
    int step = u_row * u_col;
    int fourth = u_data / (step * step * step);
    int third = (u_data - fourth * step * step * step) / (step * step);
    int second = (u_data - fourth * step * step * step - third * step * step) / step;
    int first = u_data - fourth * step * step * step - third * step * step - second * step;
    int data = first;
    vec2 base_coord = textureCoordinate;
    if(u_digitNum == 2){
        if (base_coord.x < 0.5) {
            data = second;
            base_coord.x = base_coord.x * 2.0;
        } 
        else {
            data = first;
            base_coord.x = (base_coord.x - 0.5) * 2.0;
        }
    }
    else if(u_digitNum == 4){
         if (base_coord.x < 0.25) {
            data = fourth;
            base_coord.x = base_coord.x * 4.0;
        } 
        else if(base_coord.x < 0.5 && base_coord.x >= 0.25){
             data = third;
            base_coord.x = (base_coord.x - 0.25) * 4.0;
        }
        else if(base_coord.x < 0.75 && base_coord.x >= 0.5){
            data = second;
            base_coord.x = (base_coord.x - 0.5) * 4.0;
        }
        else{
            data = first;
            base_coord.x = (base_coord.x - 0.75) * 4.0;
        }
    }    
     
    vec2 coord = getTextureCoord_rowfirst(data, base_coord, u_row, u_col);
    gl_FragColor = texture2D(inputImageTexture, coord);
}
#version 330

in  vec2 outTexCoord;
in  vec4 outColors;
out vec4 fragColor;

uniform int blit;
uniform sampler2D texture_sampler;

void main()
{
    if (blit == 1) {
        fragColor = outColors * texture(texture_sampler, outTexCoord);
    } else {
        fragColor = outColors;
    }
}
#version 330

in  vec2 outTexCoord;
in  vec4 outColors;
out vec4 fragColor;

uniform vec2 texture_offset;
uniform sampler2D texture_sampler;

void main()
{
    fragColor = outColors * texture(texture_sampler, outTexCoord + texture_offset);
}
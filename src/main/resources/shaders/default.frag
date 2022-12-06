#version 330

in  vec2 outTexCoord;
in  vec3 outColors;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = vec4(outColors.rgb,1) * texture(texture_sampler, outTexCoord);
}
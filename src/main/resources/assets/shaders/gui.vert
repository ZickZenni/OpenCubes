#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec4 colors;

out vec4 outColors;
out vec2 outTexCoord;

void main()
{
    gl_Position.xyz = position;
    gl_Position.w = 1.0;
    outColors = colors;
    outTexCoord = texCoord;
}
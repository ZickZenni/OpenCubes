#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec4 colors;

out vec4 outColors;

void main()
{
    gl_Position.xyz = position;
    gl_Position.w = 1.0;
    outColors = colors;
}
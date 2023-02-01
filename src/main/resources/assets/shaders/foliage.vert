#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec4 colors;

out vec2 outTexCoord;
out vec4 outColors;

uniform float time;
uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * modelViewMatrix *
        vec4(position.x + sin(time + 30 + (gl_VertexID * 20)) * 0.21,
        position.y + sin(time) * 0.07,
        position.z + sin(time + 90 + (gl_VertexID * 12)) * 0.19,
        1.0);
    outTexCoord = texCoord;
    outColors = colors;
}
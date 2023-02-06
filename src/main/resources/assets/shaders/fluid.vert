#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec4 colors;

out vec2 outTexCoord;
out vec4 outColors;

uniform vec3 modelPosition;
uniform float time;
uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * modelViewMatrix *
        vec4(position.x,
        position.y + sin(modelPosition.x + position.x + time * 0.82 + modelPosition.z + position.z) * 0.085,
        position.z,
        1.0);
    outTexCoord = texCoord;
    outColors = colors;
}
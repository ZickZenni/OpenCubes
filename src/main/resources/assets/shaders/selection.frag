#version 330

out vec4 fragColor;
uniform float time;

void main()
{
    float alpha = (sin(time * 8) + 1) / 2;
    fragColor = vec4(0.2f, 0.2f, 0.2f, alpha * 0.2f + 0.1f);
}
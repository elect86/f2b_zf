#version 330

layout(std140) uniform;

layout(location = 0) in vec3 position;

uniform mat4 camera;

void main()
{
    gl_Position = mat4(1f) * vec4(position, 1.0);
}

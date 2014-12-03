#version 330

uniform sampler2DRect colorTexture;
uniform vec3 backgroundColor;

out vec4 outputColor;

void main(void)
{
	vec4 frontColor = texture(colorTexture, gl_FragCoord.xy);

	outputColor.rgb = frontColor.rgb + backgroundColor * frontColor.a;
	outputColor.a = 1.0f;
}
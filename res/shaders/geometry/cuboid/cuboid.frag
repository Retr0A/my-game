#version 400 core

in vec3 fragPosition;
in vec3 fragColor;
in vec3 fragNormal;

out vec4 out_Color;

uniform vec3 lightAmbient;
uniform vec3 lightColor;
uniform vec3 lightPosition;
uniform mat4 projectionMatrix;

void main(void){
    vec3 lightPos = normalize(lightPosition);


    out_Color = vec4(1, 1, 1, 1);
}
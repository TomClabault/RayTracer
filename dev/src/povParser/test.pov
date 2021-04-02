sphere { <1, 2, 3>, 4.0
  pigment { color rgb 1 }
  finish {
    ambient 0 diffuse 0
    specular 0.7  roughness 0.01
    reflection { 0.7 metallic }
  }
}
triangle
{
    <0, 0, 0>, <1, 1, 1>, <2, 2, 2>
}

plane {
    <0, 1, 0>, 4
}

box
{
    <0, 0, 0>, <1, 1, 1>
}
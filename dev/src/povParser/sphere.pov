sphere { <1.5,0.7,1>, 0.7
  pigment { color rgb 1 }
  finish {
    ambient 0 diffuse 0
    specular 0.7  roughness 0.01
    reflection { 0.7 metallic }
  }
}

triangle
{
    <1, 2, 3>, <4, 5, 6>, <7, 8, 9>
    pigment {color rgb 1}
    finish
    {
        ambient 1
        diffuse 0
    }
}

plane
{
    <0, 0, 0> 1
}

box
{
    <1, 1, 1> <2, 2, 2>
}


sphere { <0.5, 1.5, 2.5>, 0.7
  pigment { color rgb <0.1, 0.2, 0.3>}
  finish {
    ambient 0.5 diffuse 0.3
    specular 0.7
    reflection 0.6
    phong_size 60
  }
}

camera
{
  location <0, 0.5, -1.5>
  look_at <0.5, 1.5, 2.5>
}

light_source
{
    <2, 2, 1>, 1
}

/*
triangle
{
    <1, 2, 3>, <4, 5, 6>, <7, 8, 9>
    pigment {color rgb <0.1, 0.2, 0.3>} // or rgb 1
    finish
    {
        ambient 1
        diffuse 0
        phong 5
    }
}
*/

plane
{
    <0, 1, 0>, -1
    finish
    {
        ambient 1
        diffuse 1
    }
    pigment
    {
        color rgb <0.2, 0.2, 0.2>
    }
}

box
{
    <1, 1, -1>, <2, 2, -2>
    pigment
    {
        color rgb 1
    }
    finish
    {
        specular 1
        phong_size 64
    }
}

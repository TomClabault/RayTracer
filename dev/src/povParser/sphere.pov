sphere
{
    <-2, -0.65, -5>, 0.35
    pigment
    {
        checker pigment{ color rgb 0}, pigment{color rgb <1, 1, 0>, size }
    }
    finish
    {
        ambient 1
        diffuse 0.75
        specular 0.05
        phong_size 1
    }
}

sphere
{
    <1.25, 0.5, -6>, 1
    pigment
    {
        color Clear
    }
    finish
    {
        ambient 1
        diffuse 0
        reflection 0
        specular 0
    }

    interior
    {
        ior 1.5
    }
}

sphere
{
    <0, 1.5, -6>, 0.5
    pigment
    {
        color rgb <0.19, 0.19, 0.19>
    }
    finish
    {
        ambient 1
        diffuse 1
        reflection 0.5
        phong_size 3
        specular 0.125
        roughness 0.64
    }
}

camera
{
  location <0, 0.5, -1.5>
  look_at <0.000, 0.5, -2.5>
  angle 60
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
        checker pigment {color rgb <1, 1, 1>}, pigment {color rgb 0}
    }
}
/*
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
*/
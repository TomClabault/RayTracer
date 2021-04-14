sphere 
{ 
	<-5.0, -0.5, -15>, 0.5
	pigment { color rgb <0.663, 0.435, 0.031>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0
		phong_size 1
		roughness 0.5
	}
}

sphere 
{ 
	<-3.0, -0.5, -15>, 0.5
	pigment { color rgb <0.392, 0.263, 0.024>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0
		phong_size 1
		roughness 0.5
	}
}

sphere 
{ 
	<-1.0, -0.5, -15>, 0.5
	pigment { color rgb <0.196, 0.133, 0.020>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0
		phong_size 1
		roughness 0.5
	}
}

sphere 
{ 
	<1.0, -0.5, -15>, 0.5
	pigment { color rgb <0.067, 0.051, 0.012>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0
		phong_size 1
		roughness 0.5
	}
}










sphere 
{ 
	<-5.0, -0.5, -12>, 0.5
	pigment { color rgb <0.663, 0.435, 0.031>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.125
		phong_size 3
		roughness 0.75
	}
}

sphere 
{ 
	<-3.0, -0.5, -12>, 0.5
	pigment { color rgb <0.392, 0.263, 0.024>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.125
		phong_size 3
		roughness 0.75
	}
}

sphere 
{ 
	<-1.0, -0.5, -12>, 0.5
	pigment { color rgb <0.196, 0.133, 0.020>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.125
		phong_size 3
		roughness 0.75
	}
}

sphere 
{ 
	<1.0, -0.5, -12>, 0.5
	pigment { color rgb <0.067, 0.051, 0.012>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.125
		phong_size 3
		roughness 0.75
	}
}















sphere 
{ 
	<-5.0, -0.5, -9>, 0.5
	pigment { color rgb <0.663, 0.435, 0.031>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.651
		phong_size 81
		roughness 0.9
	}
}

sphere 
{ 
	<-3.0, -0.5, -9>, 0.5
	pigment { color rgb <0.392, 0.263, 0.024>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.651
		phong_size 81
		roughness 0.9
	}
}

sphere 
{ 
	<-1.0, -0.5, -9>, 0.5
	pigment { color rgb <0.196, 0.133, 0.020>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.651
		phong_size 81
		roughness 0.9
	}
}

sphere 
{ 
	<1.0, -0.5, -9>, 0.5
	pigment { color rgb <0.067, 0.051, 0.012>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 0.651
		phong_size 81
		roughness 0.9
	}
}













sphere 
{ 
	<-5.0, -0.5, -6>, 0.5
	pigment { color rgb <0.663, 0.435, 0.031>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 1
		phong_size 192
		roughness 1
	}
}

sphere 
{ 
	<-3.0, -0.5, -6>, 0.5
	pigment { color rgb <0.392, 0.263, 0.024>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 1
		phong_size 192
		roughness 1
	}
}

sphere 
{ 
	<-1.0, -0.5, -6>, 0.5
	pigment { color rgb <0.196, 0.133, 0.020>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 1
		phong_size 192
		roughness 1
	}
}

sphere 
{ 
	<1.0, -0.5, -6>, 0.5
	pigment { color rgb <0.067, 0.051, 0.012>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.5
		specular 1
		phong_size 192
		roughness 1
	}
}

camera
{
  location <-2, 4, -1>
  look_at <-2, 0, -8>
  angle 40
}


light_source
{
    <-3, 6, 1>, 1
}

/*
light_source
{
    <3, 6, 1>, 1
}
*/

light_source
{
    <-2, 6, -10.5>, 1
}

plane
{
    <0, 1, 0>, -1
    finish
    {
        ambient 1
        diffuse 0.75
		specular 0.05
		phong_size 1
		reflection 0.05
    }
    pigment
    {
        color rgb <0.5, 0.5, 0.5>
    }
	interior
	{
		ior 1.2
	}
}

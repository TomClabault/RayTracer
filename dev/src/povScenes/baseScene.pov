camera
{
  location <0, 0.5, 0.320>
  look_at <0, 0.5, -1.320>
  angle 40
}


light_source
{
    <2, 2, 1>, 1
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
    }
    pigment
    {
        checker pigment{ color rgb 0.125}, pigment{color rgb 0.59}, size 1
    }
}

/*
mirror
*/
sphere 
{ 
	<-1.25, 0.5, -6>, 1
	pigment { color rgb 0}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.75
		specular 0.75
		phong_size 192
	}
}

/*
Rough gold
*/
sphere 
{ 
	<0, 1.5, -6>, 0.5
	pigment { color rgb <0.66, 0.43, 0.031>}
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

/*
glass
*/
sphere 
{ 
	<1.25, 0.5, -6>, 1
	pigment { color Clear }
	finish 
	{
		ambient 1 
		roughness 0.5
	}
	interior
	{
		ior 1.474
	}
}

/*
Green glassy derriere le verre
*/
sphere 
{ 
	<1.5, 0.5, -7.13>, 0.2
	pigment { color rgb <0, 1, 0>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.03
		specular 1
		phong_size 256
	}
	interior
	{
		ior 1.5
	}
}

/*
trois glassy entre le mirror et le verre
*/
sphere 
{ 
	<0, -0.5, -6>, 0.5
	pigment { color rgb <1, 0, 0>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.03
		specular 1
		phong_size 256
	}
	interior
	{
		ior 1.5
	}
}

sphere 
{ 
	<-0.75, -0.75, -6>, 0.2
	pigment { color rgb <1, 0.25, 0>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.03
		specular 1
		phong_size 256
	}
	interior
	{
		ior 1.5
	}
}

sphere 
{ 
	<0.75, -0.75, -6>, 0.2
	pigment { color rgb <1, 0.25, 0>}
	finish 
	{
		ambient 1 
		diffuse 1
		reflection 0.03
		specular 1
		phong_size 256
	}
	interior
	{
		ior 1.5
	}
}

/*
	deux spheres checkerboard sur les cotes
*/

sphere 
{ 
	<-2, -0.65, -5>, 0.35
	pigment 
	{ 
		checker pigment {color rgb 0}, pigment {color rgb <1, 1, 0>}, size 12
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
	<2, -0.65, -5>, 0.35
	pigment 
	{ 
		checker pigment {color rgb <1, 0, 0>}, pigment {color rgb <0.35, 0, 0>}, size 12
	}
	finish
    {
        ambient 1
        diffuse 0.75
		specular 0.05
		phong_size 1
    }
}

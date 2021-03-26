sphere { <1.5,0.7,1>, 0.7
  pigment { color rgb 1 }
  finish {
    ambient 0 diffuse 0
    specular 0.7  roughness 0.01
    reflection { 0.7 metallic }
  }
}
/*
triangle
{
    <0, 0, 0>, <1, 1, 1>, <2, 2, 2>
}
*/
/*
polygon
{
    Number_Of_Points, <Point_1> <Point_2>...<Point_n>
}*/
/*
<p>bonjour</p>
               <                   p                   >
[exterieur]------->[tag ouvrant] -------->[balise p]-------->[tag fermant]------>[contenu]*/
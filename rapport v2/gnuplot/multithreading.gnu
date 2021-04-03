set terminal png

set output 'MultithreadingGraph.png'

set title "Impact du nombre de thread sur les performances"
set xlabel "Nombre de threads"
set ylabel "Temps de rendu (ms)"

plot 'multithreading.txt' u 1:3 w l t "Résolution: 256*256px", 'multithreading.txt' u 1:4 w l t "Résolution: 512*512px", 'multithreading.txt' u 1:5 w l t "Résolution: 1280*720px"
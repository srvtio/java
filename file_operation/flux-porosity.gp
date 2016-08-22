#-----------------------------------------------------
set terminal postscript eps
set output "flux-porosity3.eps"
set xrange[0.40:0.90]
set yrange[0.1:20]
#set yrange[1.5:2.5]

set xlabel "porosity"
set ylabel "-Mass Flux/dp"
set size ratio 1

set key left top

set logscale y
set logscale x

set xtics (0.5,0.6,0.65,0.7,0.75,0.8)

set style line 1 lc rgb "black" lw 2 linetype 1
set style line 2 lc rgb "black" lw 1.2 linetype 2 

j(x) = e*x**g
e = 1
f = -1
g = 1
fit j(x) "flux-porosity.dat" u 1:2 via e,g

plot "flux-porosity.dat" u 1:2 w p pt 9 ps 1.2 lc rgb "black" notitle 
h(x) w l linestyle 1 

unset output
reset
#-----------------------------------------------------


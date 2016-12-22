set terminal postscript eps
set output "cluster.eps"
set datafile separator ","
set key bottom left
FILES = system("ls -1 plot/*.csv")
plot for [data in FILES] data using 2:3:4 with points pointtype 7 linecolor rgb variable title columnhead,\
     "plot/noise" using 2:3:4 with points pointtype 2 linecolor rgb variable title columnhead,\
#plot for [data in FILES] data using 3:2  with points pointsize 5 lc palette



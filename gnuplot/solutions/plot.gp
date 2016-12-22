#set terminal postscript eps
#set output "test.eps"
set xrange [-40:60]
set yrange [-20:70]
set datafile separator ","
set key bottom left
set style line 1 lc rgb "blue" lt 1 lw 1
FILES = system("ls -1 test/*.csv")
plot for [data in FILES] data every ::2 using 2:3 with lines notitle,\
#	"../../instance/gifts.csv" using 3:2 with points pointtype 6
pause -1

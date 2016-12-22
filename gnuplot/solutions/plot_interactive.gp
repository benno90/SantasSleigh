#set terminal postscript eps
#set output "test.eps"

set xrange [-60:0]
set yrange [60:90]
set datafile separator ","
set key bottom left
set style line 1 lc rgb "blue" lt 1 lw 4

# number of lines 
num_lines=100
# number of neighbours
num_neigh=100
nn= num_neigh*2

# Plot the first line in the data-file:
#line_index = 1
#plot "test/0.csv" every ::0::line_index using 2:3 with lines ls 1 notitle
#plot  "../../instance/gifts.csv" using 3:2 with points pointtype 6

do for [line_index = 1:num_lines-1] { 
a=line_index
b=line_index
plot for [k = 5:nn:2] c=k+1 "south/1.csv" every ::a::b using k:c with points pointtype 7 lc rgb "black" notitle,\
			    "../../instance/gifts.csv" using 3:2 with points pointtype 6 lc rgb "blue" notitle,\
      			    "south/1.csv" every ::0::line_index using 2:3 with lines ls 1 notitle,\

  #pause -1
   pause 0.5
}
# replot "test/0.csv" every ::0::line_index using 2:3 with lines ls 1 notitle

#FILES = system("ls -1 test/*.csv")
#plot for [data in FILES] data using 2:3 with lines ls 1 title columnhead,\


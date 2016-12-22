#set terminal postscript eps
#set output "eval.eps"
set datafile separator ","
set key bottom left

plot "paramTest.csv" with lines

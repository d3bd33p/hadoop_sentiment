colors <- c("green","orange")
months <- c("Mar","Apr","May","Jun","Jul")
regions <- c("East","West","North")

# Create the matrix of the values.
Values <- matrix(c(2,9,3,11,9,4,8,7,3,12,5,2,8,10,11),nrow = 3,ncol = 5,byrow = TRUE)

# Give the chart file a name.
png(file = "barchart_stacked.png")

# Create the bar chart.
barplot(Values,main = "total revenue",names.arg = months,xlab = "month",ylab = "revenue",
        col = colors)

# Add the legend to the chart.
legend("topleft", regions, cex = 1.3, fill = colors)

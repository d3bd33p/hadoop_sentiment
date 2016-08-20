#Taking datas of any 5 lines from "mydata"-dataset; values taken=actual values*100;
data <- structure(list(line1= c(11.1, 12.3), line2 = c(14.9,0), line6 = c(30.2,19.2), line11 = c(19.2,7.5),line18 = c(45.5,16.7)), .Names = c("line1", "line2", "line6", "line11", "line18"), class = "data.frame", row.names = c(NA, -2L))
attach(data)
print(data)
colours <- c("dark blue", "red")
barplot(as.matrix(data), main="Bar Chart for Sentiment Analysis", ylab = "Overall Sentiment", xlab = "Positive vs Negative Sentiments", cex.lab = 1.5, cex.main = 1.4, beside=TRUE, col=colours)
warnings()

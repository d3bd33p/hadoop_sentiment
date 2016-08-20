setwd("C://Users//NEW//Desktop//big_data_proj")
mydata<-read.csv("test1.csv")
mydata

tot_pos<-sum(mydata$positive)
tot_pos
tot_neg<-sum(mydata$negative)
tot_neg
tot_overall<-sum(mydata$overall)
tot_overall

division <- c(tot_pos,tot_neg)
lbls <- c("Positve Sentiment", "Negative Sentiment")
percentage <- round(division/sum(division)*100)
lbls <- paste(lbls, percentage) # add percents to labels 
lbls <- paste(lbls,"%",sep="")
pie(division, labels = lbls, col=rainbow(length(lbls)),main="Pie Chart of Overall Sentiment")


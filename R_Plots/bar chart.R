setwd("C://Users//NEW//Desktop//big_data_proj")
mydata<-read.csv("test2.csv")

counts<-table(mydata$positive,mydata$negative)

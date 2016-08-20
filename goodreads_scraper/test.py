# Required Libraries
import urllib
import os.path
import re

# Output Format
import json

# Make sure you have BeautifulSoup installed
from bs4 import BeautifulSoup

# The URL
url = "https://www.goodreads.com/quotes/list/18654747-shadab-zafar"

# The URL will be saved to this file
fileName = "GRQuotesPage1.html"

# Doownload file only if it does not exist.
if os.path.isfile(fileName):
	print("File Exists. Will Be Read.\n")
else:
	print("File Does Not Exists. Will Be Downloaded.")
	site = urllib.urlopen(url)
	data = site.read()

	f = open(fileName, "wb")
	f.write(data)
	f.close()
	print("File Downloaded.")

# Create the soup.
f = open(fileName)
soup = BeautifulSoup(f)
f.close()

# The Debug file
opFile = open("debug.txt", 'w')

# User Metadata
title = soup.find('title').string.replace("\n", " ")
titleScrape = re.findall('\((.*?)\)', title)

# Username and Total Quotes
user = titleScrape[0]
totalQuotes = re.search('(\d+)$', titleScrape[2]).group(1)

# While Testing and Debugging
# quit()

# Quote text, author name and URL
quoteText = soup.findAll('div', attrs={'class':'quoteText'})

# print (len(quoteText))

# Quote URL
quoteFooterRight = soup.findAll('div', attrs={'class':'right'})

# Begin Scraping
for (q,r) in zip(quoteText, quoteFooterRight):

	quote = q.contents[0].encode('ascii', 'ignore').decode('ascii', 'ignore')

	

	# json.dumps()

	opFile.write(re.sub("  +", "", quote.replace("\n", "")) + "\n")


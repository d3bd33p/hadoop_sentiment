from vaderSentiment import sentiment as vaderSentiment
	#note: depending on how you installed (e.g., using source code download versus pip install), you may need to import like this:
	#from vaderSentiment.vaderSentiment import sentiment as vaderSentiment

	# --- example sentences -------
with open('input.txt','r') as f:
    sentences = f.readlines()
			
			
for sentence in sentences:
		print sentence,
		vs = vaderSentiment(sentence)
		print "\n\t" + str(vs)
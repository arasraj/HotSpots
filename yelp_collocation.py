import re
import tfidf2
import nltk
import urllib2
from nltk.collocations import *

def get_collocation(url):
  conn = urllib2.urlopen(url)
  raw_html = conn.read()
  conn.close()
  tokens = parse_tokenise_comments(raw_html.lower())
  #print text.collocations(window_size=3)
  #if comments:
  	#print '\n'.join(comments)
  	#print 'Len of comments: ' + str(len(comments))

  #tfidf = TfIdf()
  #tfidf2.doit(' '.join(comments))
  #rslt = tfidf.get_doc_keywords(' '.join(comments))
  #for term in rslt:
    #print term 
  
  ignored_words = nltk.corpus.stopwords.words('english')
  finder = BigramCollocationFinder.from_words(tokens)
  finder.apply_freq_filter(3)
  finder.apply_word_filter(lambda word: len(word) < 3 or word.lower() in ignored_words)
  bigram_measures = nltk.collocations.BigramAssocMeasures()
  #results = finder.nbest(bigram_measures.pmi, 10)
  results = finder.nbest(bigram_measures.poisson_stirling, 5)
  
  #for result in results:
  	#print result[0], result[1]
  #print results[:2]
  return results[:2]

def parse_tokenise_comments(html):
  comments = re.findall(r'review_comment description\s*\w*">(.+)</p>', html)
  tokens = nltk.word_tokenize(' '.join(comments))
  text = nltk.Text(tokens)
  return text


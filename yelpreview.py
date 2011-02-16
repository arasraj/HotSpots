import urllib2
import base64
import json
from yelp_collocation import get_collocation

def get_reviews(name, lat, lng):
  name = name.replace(' ', '%20')
  request = urllib2.Request("http://api.yelp.com/business_review_search?term=%s&lat=%s&long=%s&radius=5&limit=5&ywsid=a2Kaon58LY1zvE5YyBNoUw" % (name, lat, lng))
  result = urllib2.urlopen(request)

  content = result.read()
  #print content

  yelp_json = json.loads(content)
  #print yelp_json
  #print "\n\n"

  #TODO: check status codes
  status_code = yelp_json['message']['code']
  print 'Status Code: ' + str(status_code)
  yelp_results = yelp_json['businesses']
  if not(yelp_results and yelp_results[0]['review_count'] >= 15):
    return (False, None)

  processed_json = process_json(yelp_results)

  return (True, ''.join(processed_json))

def process_json(yelp_results):
  businesses = yelp_results[0];
  review_count = businesses['review_count']
  reviews = businesses['reviews']
  url = businesses['url']
  photo_url = businesses['photo_url']
  small_photo_url = businesses['photo_url_small']
  address = businesses['address1']
  print 'URL: ' + url

  if int(review_count) > 200: review_count = '200'

  url = url + '?rpp=%s&sort_by=revelance_desc' % review_count
  map_words_tuples = get_collocation(url)
  map_words = map_words_tuples[0]
  for word in businesses['name'].split():
    if (word.lower() in map_words and len(map_words_tuples) > 1):
    	map_words = map_words_tuples[1]


  yelp_json = []
  yelp_json.append('"yelp_name":"%s",' % (businesses['name']))
  #print businesses['name'] + '\n'
  yelp_json.append('"reviews":[')
  for rvw in reviews:
    yelp_json.append('{"rating":"%s",' % (str(rvw['rating'])))
    text_excerpt = rvw['text_excerpt'].replace('"', '')
    yelp_json.append('"text":"%s",' % (text_excerpt))
    yelp_json.append('"username":"%s"}' % (rvw['user_name'])) 
    yelp_json.append(',')

  yelp_json.pop()
  yelp_json.append('],')
  yelp_json.append('"address":"%s",' % (address))
  yelp_json.append('"photo_url":"%s",' % (photo_url))
  yelp_json.append('"small_photo_url":"%s",' % (small_photo_url))
  yelp_json.append('"review_count":"%s",' % (review_count))
  #if map_words: 
  yelp_json.append('"map_word":"%s %s",' % (map_words[0], map_words[1]))
  #else:
  #	yelp_json.append('"map_word":"%s",' % (businesses['name']))

  return yelp_json




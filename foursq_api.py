import tornado.httpserver
import tornado.ioloop
import tornado.web
import urllib2
import base64
import json
import yelpreview

class MainHandler(tornado.web.RequestHandler):
  def get(self):
    #self.write("Hellow, world!")
    KEY = ''
    PASSWORD = ''

    lat = self.get_argument("lat")
    lng = self.get_argument("lng")
    #self.write(str(lat))
    #self.write(str(lng))

    reqStr = "http://api.foursquare.com/v1/venues.json?geolat=%s&geolong=%s&l=8" % (str(lat), str(lng))
    request = urllib2.Request(reqStr)
    base64string = base64.encodestring('%s:%s' % (KEY, PASSWORD)).replace('\n', '')
    request.add_header("Authorization", "Basic %s" % base64string)   
    result = urllib2.urlopen(request)

    apiJson = result.read()
    parsedJson =  json.loads(apiJson)
    parsedJson = parsedJson['groups']

    hotspots = []
    hotspots.append('[')

    for group in parsedJson:
      for venue in group['venues']:
        name = venue['name']
      	(ratedEnough, venue_reviews) = yelpreview.get_reviews(name, lat, lng)
        if (ratedEnough and venue['distance'] < 800):
          #if int(venue['stats']['herenow']) > 0 and int(venue['distance']) < 800:
          hotspots.append('{"name":"' + name + '",')
          #yelp_rvws = yelpreview.get_reviews(name, lat, lng)
          hotspots.append(venue_reviews)
          hotspots.append('"herenow":"' + venue['stats']['herenow'] + '",')
          hotspots.append('"geolat":"' + str(venue['geolat']) + '",')
          hotspots.append('"geolong":"' + str(venue['geolong']) + '",')
          hotspots.append('"distance":"' + str(venue['distance']) + '"}')
          hotspots.append(',')
     
    hotspots.pop()

    if (len(hotspots) == 0):
      hotspots.append('[{"name":"No HotSpots", "":"", "":""}')

    hotspots.append(']')
    for spot in hotspots:
      self.write(spot)


      

application = tornado.web.Application([(r"/hotspots", MainHandler),])

if __name__ == "__main__":
  http_server = tornado.httpserver.HTTPServer(application)
  http_server.listen(8888)
  tornado.ioloop.IOLoop.instance().start()

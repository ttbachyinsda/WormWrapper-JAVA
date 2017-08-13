import requests
import time
import json
import datetime
now = datetime.datetime.now()
timestr = now.strftime('%Y-%m-%d-%H-%M-%S')
outfile = open(timestr+".txt", "w+",encoding='utf-8')
js = 0
while True:
    while True:
        print(time.localtime(time.time()))
        try:
            c = requests.get("http://120.55.238.158/api/live/simpleall")
            d = json.loads(c.text, encoding='utf-8')
            timestampstr = str(time.time())
            i = 0
            print(len(d["lives"]))
            for element in d["lives"]:
                i += 1
                location = str(element["creator"]["location"])
                if (location == ''):
                    location = 'EMPTY'
                res = timestampstr+' '+str(i)+' '+str(element["creator"]["id"])+' '+location\
                      +' '+str(element["creator"]["gender"])+' '+str(element["creator"]["level"])+' '+str(element["online_users"])
                #break
                #print(res)
                outfile.write(str(res)+'\n')
            break
        except Exception as e:
            print(e)
            continue
    time.sleep(60)
    js += 1
    if (js == 30):
        outfile.close()
        js = 0
        now = datetime.datetime.now()
        timestr = now.strftime('%Y-%m-%d-%H-%M-%S')
        outfile = open(timestr + ".txt", "w+")
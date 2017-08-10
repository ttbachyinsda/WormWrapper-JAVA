import requests
import time
import json
while True:
    outfile = open(str(time.time())+".txt", "w")
    while True:
        print(time.localtime(time.time()))
        try:
            c = requests.get("http://120.55.238.158/api/live/simpleall")
            d = json.loads(c.text, encoding='utf-8')
            for element in d["lives"]:
                s = element["id"]
                outfile.write(str(s)+'\n')
            break
        except Exception as e:
            continue
    outfile.close()
    time.sleep(60)
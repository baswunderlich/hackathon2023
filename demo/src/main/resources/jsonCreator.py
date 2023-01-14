# Customer.json, Knowledge json, battery json
import json
import random

yearInUnixTime = 31536000
year2018inUnix = 1514761200
year2021UnixTime = 1610574020

customerNames = ["VELITY","PERMADYNE","OVERPLEX",
            "VENDBLEND","ENERSOL","ZENTIME","VALREDA",
            "ISODRIVE","MARTGO"]

batteryProducer = ["PROFLEX","ZILPHUR","AQUAFIRE","PHORMULA"]
batteryID = [12345, 42, 987, 21, 333]

customerReqCapacity = [6500, 10000, 4000, 20000, 7000,
                    4500, 12000, 8000, 9000, 2000]

producerCapacitys = [10000, 5000, 20000, 13000]

def createJson(dictAsJson, filename):
    newDictAsJson = "[\n" + dictAsJson[:-2] + "\n]"
    # Writing to sample.json
    with open("./"+filename+".json", "w") as outfile:
        outfile.write(newDictAsJson)

customerJson = ""
for i in range(len(customerNames)):
    customer = {
        "customerName": customerNames[i],
        "minCapacity": customerReqCapacity[i],
        "minWirkungsgrad": random.randint(20, 65),
        "erwarteteLebensdauer": yearInUnixTime * random.randint(2, 7),
        "chargingCycles": random.randint(2000, 8000),
        "preisProBatterie": random.randint(500, 10000)
    }
    # Serializing json
    customerJson += json.dumps(customer, indent=4)+",\n"
# Store the json
createJson(customerJson, "customers")

batteryJson = ""
for i in range(len(batteryProducer)):
    battery = {
        "id": str(batteryID[i]),
        "hersteller": batteryProducer[i],
        "wirkungsgrad": random.randint(20, 80),
        "capacity": producerCapacitys[i],
        "chargingCycles" : random.randint(2000, 8000),
        "herstellDatum" : random.randint(year2018inUnix, year2021UnixTime)
    }
    # Serializing json
    batteryJson += json.dumps(battery, indent=4)+",\n"
# Store the json
createJson(batteryJson, "batteryPass")

knowledgeJson = ""
for i in range(len(batteryProducer)):
    knowledge = {
        "hersteller": batteryProducer[i],
        "wirkungsgrad": random.randint(20, 80),
        "lebensdauerNachMessung": -1,
        "lebensdauerGesamt" : random.randint(8, 15) * yearInUnixTime,
        "lebensdauerBisMessung" : random.randint(5, 12) * yearInUnixTime,
        "chargingCycles" : random.randint(2000, 17000)
    }
    # Serializing json
    knowledgeJson += json.dumps(knowledge, indent=4)+",\n"
# Store the json
createJson(knowledgeJson, "knowledge")

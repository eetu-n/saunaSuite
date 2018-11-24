from flask import Flask, render_template
from api.api_class import VaisalaApi
app = Flask(__name__)

apiCaller = VaisalaApi()

@app.route('/')
def index():
    temp = apiCaller.get_latest_device("Bench2").json()[0]["Measurements"]["Temperature"]["value"]
    humi = apiCaller.get_latest_device("Bench2").json()[0]["Measurements"]["Relative Humidity"]["value"]
    loylyThrown = 8
    logsUsed = 5
    return render_template('index.html', curTemp=temp, curHumi=humi, loyly=loylyThrown, logs=logsUsed)


if __name__ == '__main__':
    app.run()

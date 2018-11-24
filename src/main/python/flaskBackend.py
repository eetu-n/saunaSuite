from flask import Flask, render_template, request
from api.wrappers import ApiWrappers
from datetime import datetime

app = Flask(__name__)

apiCaller = ApiWrappers()


@app.route('/', methods=["POST", "GET"])
def index():

    temp = apiCaller.get_temp()
    humi = apiCaller.get_humidity()
    loyly_thrown = 8
    logs_used = 5
    next_log = "15:30"
    warm_until = "19:32"
    time_end = str(datetime.now().hour) + ":" + str(datetime.now().minute)
    time_begin = str(datetime.now().hour - 6) + ":" + str(datetime.now().minute)

    if request.method == 'POST':
        time_begin = request.form['timeBegin']
        time_end = request.form['timeEnd']

    return render_template('index.html', curTemp=temp, curHumi=humi, loylyThrown=loyly_thrown, logsUsed=logs_used,
                           nextLog=next_log, warmUntil=warm_until, timeBegin=time_begin, timeEnd=time_end)


if __name__ == '__main__':
    app.run()

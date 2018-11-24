from flask import Flask, render_template
from api.wrappers import ApiWrappers

app = Flask(__name__)

apiCaller = ApiWrappers()


@app.route('/')
def index():
    temp = apiCaller.get_temp()
    humi = apiCaller.get_humidity()
    loylyThrown = 8
    logsUsed = 5
    return render_template('index.html', curTemp=temp, curHumi=humi, loyly=loylyThrown, logs=logsUsed)


if __name__ == '__main__':
    app.run()

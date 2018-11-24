from api.api_class import VaisalaApi

apiCaller = VaisalaApi()


class ApiWrappers():

    def get_temp(self):
        return apiCaller.get_latest_device("Bench2").json()[0]["Measurements"]["Temperature"]["value"]

    def get_humidity(self):
        return apiCaller.get_latest_device("Bench2").json()[0]["Measurements"]["Relative humidity"]["value"]

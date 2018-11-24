import requests


class VaisalaApi():

    API_URL = 'https://apigtw.vaisala.com/hackjunction2018/saunameasurements/'
    HEADERS = {'Accept': 'application/json'}
    METHOD = 'GET'
    DEVICES = [
        'Ceiling1',
        'Ceiling2',
        'Stove1',
        'Stove2',
        'Bench1',
        'Bench2',
        'Bench3',
        'Floor1',
        'Doorway1',
        'Outdoor1'
    ]

    def __init__(self, debug=True, sess=None):
        self.__debug = debug
        self.__init_session(sess)

    def __init_session(self, sess):
        if sess is not None:
            self.sess = sess
        else:
            self.sess = requests.Session()
            self.sess.headers.update(VaisalaApi.HEADERS)

    def end_sess(self):
        if self.sess is not None:
            self.sess.close()

    def check_device(self, device):
        return device in VaisalaApi.DEVICES

    def get_device_list_string(self):
        return ['\t{}\n'.format(d) for d in VaisalaApi.DEVICES]

    def __sanity(self, device, limit=None):
        if not self.check_device(device):
            self.__print_debug(
                    'Device "{}" is not in the supported list:\n{}'
                    .format(device, self.get_device_list_string()))
            return True
        if limit is not None and 1000 <= limit <= 1:
            self.__print_debug('Limit was not between [1, 1000]: {}'
                               .format(limit))
            return True
        return False

    def __print_debug(self, message):
        if self.__debug:
            print(message)

    def __do_request(self, url_append, params):
        self.sess.params.update(params)
        return self.sess.request(url=VaisalaApi.API_URL + url_append,
                                 method=VaisalaApi.METHOD)

    def get_latest_device(self, device, limit=1):
        if self.__sanity(device, limit):
            return None

        tmp_params = {'SensorID': device, 'limit': limit}
        return self.__do_request('latest', tmp_params)

    def get_history_data(self, device, after, before):
        if self.__sanity(device):
            return None

        tmp_params = {
                'SensorID': device,
                'after': after,
                'before': before
        }
        return self.__do_request('history', tmp_params)

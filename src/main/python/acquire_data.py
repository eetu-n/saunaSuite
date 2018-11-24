import file_handling

from api_class import VaisalaApi


test_timestamp = 1543010784919
time_diff = 100000

api = VaisalaApi()

for device in VaisalaApi.DEVICES:
    filename = '{}.json'.format(device)
    history = api.get_history_data(device, test_timestamp - time_diff, test_timestamp).json()
    file_handling.write_vaisala_history(history, filename)

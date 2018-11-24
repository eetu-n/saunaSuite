import json

from datetime import datetime


def __filename_sanity(filename):
    if filename is None:
        time_name = datetime.now().replace(' ', '_')
        filename = 'data-{}.txt'.format(time_name)
    return filename


def json_to_file(json_data, filename=None, indent=None):
    filename = __filename_sanity(filename)
    with open(filename, 'w+') as fp:
        json.dump(json_data, fp, indent=indent)


def file_append_json(json_data, filename=None):
    filename = __filename_sanity(filename)
    with open(filename, 'a+') as fp:
        json.dump(json_data, fp)
        fp.write('\n')


def write_vaisala_history(vaisala_data, filename=None):
    filename = __filename_sanity(filename)
    for data in vaisala_data:
        file_append_json(data, filename)

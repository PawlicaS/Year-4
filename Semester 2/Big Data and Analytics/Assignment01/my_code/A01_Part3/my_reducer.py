#!/usr/bin/python
# --------------------------------------------------------
#
# PYTHON PROGRAM DEFINITION
#
# The knowledge a computer has of Python can be specified in 3 levels:
# (1) Prelude knowledge --> The computer has it by default.
# (2) Borrowed knowledge --> The computer gets this knowledge from 3rd party libraries defined by others
#                            (but imported by us in this program).
# (3) Generated knowledge --> The computer gets this knowledge from the new functions defined by us in this program.
#
# When launching in a terminal the command:
# user:~$ python3 this_file.py
# our computer first processes this PYTHON PROGRAM DEFINITION section of the file.
# On it, our computer enhances its Python knowledge from levels (2) and (3) with the imports and new functions
# defined in the program. However, it still does not execute anything.
#
# --------------------------------------------------------

# ------------------------------------------
# IMPORTS
# ------------------------------------------
import sys
import codecs


# ------------------------------------------
# FUNCTION my_reduce
# ------------------------------------------
def my_reduce(my_input_stream, my_output_stream, my_reducer_input_parameters):
    processed_data = []
    station_data = set()
    output_data = dict()

    for row in my_input_stream:
        row = row.translate(str.maketrans('()\n', '   ')).replace('\t', ','). replace(' '*2, ' ')
        processed_data.append(row.split(','))

    for item in processed_data:
        item[0] = item[0].strip()
        item[1] = item[1].strip()
        item[2] = item[2].strip()
        station_data.add(item[0])

    for station in station_data:
        start_station_count, stop_station_count = 0, 0
        for item in processed_data:
            if station == item[0]:
                start_station_count += int(item[1])
                stop_station_count += int(item[2])
        output_data[station] = start_station_count, stop_station_count

    output_data = sorted(output_data.items())

    for item in output_data:
        # station_name \t (total_trips_starting_from_the_station, total_trips_finishing_at_the_station) \n
        my_output_stream.write(f"{item[0]} \t ({item[1][0]}, {item[1][1]}) \n")

# ---------------------------------------------------------------
#           PYTHON EXECUTION
# This is the main entry point to the execution of our program.
# It provides a call to the 'main function' defined in our
# Python program, making the Python interpreter to trigger
# its execution.
# ---------------------------------------------------------------
if __name__ == '__main__':
    # 1. We collect the input values
    my_input_stream = sys.stdin
    my_output_stream = sys.stdout
    my_reducer_input_parameters = []

    # 5. We call to my_reduce
    my_reduce(my_input_stream,
              my_output_stream,
              my_reducer_input_parameters
             )


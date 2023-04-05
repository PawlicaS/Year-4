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
    processed_data, truck_data, truck_data_pairs, output_data = [], [], [], []

    for row in my_input_stream:
        row = row.replace('universal \t (', '').replace(') \n', '')
        processed_data.append(row.split(' @ '))

    for item in processed_data:
        length = len(item)
        split = len(item) // 4
        truck_data.append([item[x * length // split: (x+1) * length // split] for x in range(split)])

    for item in truck_data:
        for subitem in item:
            truck_data_pairs.append(subitem)

    i = 0
    while i < len(truck_data_pairs) - 1:
        if truck_data_pairs[i][3] != truck_data_pairs[i+1][2]:
            output_data.append([truck_data_pairs[i][1], truck_data_pairs[i][3], truck_data_pairs[i+1][0], truck_data_pairs[i+1][2]])
        i += 1

    for item in output_data:
        # By_Truck \t (time_it_was_logged_at_station2, station2_id, time_it_was_logged_at_station3, station3_id) \n
        my_output_stream.write(f"By_Truck \t ({item[0]}, {item[1]}, {item[2]}, {item[3]}) \n")


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


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
import codecs


# ------------------------------------------
# FUNCTION parse_in
# ------------------------------------------
def parse_in(input_name):
    lines = []
    my_data = []
    text = codecs.open(input_name)
    for line in text:
        lines.append(line.split())
    text.close()
    my_data.append(lines[0][0])
    my_data.append(lines[0][1])
    my_data.append(lines[1:])
    return my_data

# ------------------------------------------
# FUNCTION solve
# ------------------------------------------
def solve(my_data):
    pass


# ------------------------------------------
# FUNCTION parse_out
# ------------------------------------------
def parse_out(output_name, my_solution):
    text = ""
    for line in my_solution:
        text += f"Case #{line[0]}: {line[1]}\n"
    file = codecs.open(output_name, 'w')
    file.write(text)
    file.close()
    print("Output file created.")


# ------------------------------------------
# FUNCTION my_main
# ------------------------------------------
def my_main(input_name, output_name):
    # 1. We do the parseIn from the input file
    my_data = parse_in(input_name)

    # 2. We do the strategy to solve the problem
    my_solution = solve(my_data)

    # 3. We do the parse out to the output file
    # parse_out(output_name, my_solution)


# ---------------------------------------------------------------
#           PYTHON EXECUTION
# This is the main entry point to the execution of our program.
# It provides a call to the 'main function' defined in our
# Python program, making the Python interpreter to trigger
# its execution.
# ---------------------------------------------------------------
if __name__ == '__main__':
    # 1. Name of input and output files
    input_name = "input.txt"
    output_name = "output.txt"

    # 2. Main function
    my_main(input_name, output_name)
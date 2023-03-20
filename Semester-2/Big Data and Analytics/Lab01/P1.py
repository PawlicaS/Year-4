"""
Name: Szymon Pawlica
Student Number: R00187226
"""
# --------------------------------------------------------
#           PYTHON PROGRAM
# Here is where we are going to define our set of...
# - Imports
# - Global Variables
# - Functions
# ...to achieve the functionality required.
# When executing > python 'this_file'.py in a terminal,
# the Python interpreter will load our program,
# but it will execute nothing yet.
# --------------------------------------------------------

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
    my_solution = []
    matrix = my_data[2]
    rows = int(my_data[0])
    cols = int(my_data[1])
    row = 0

    while row < rows:
        col = 0
        while col < cols:
            if matrix[row][col] == 'o':
                matrix[row][col] = 0
                for i in range(-1, 2):
                    if rows-1 >= row+i >= 0:
                        for j in range(-1, 2):
                            if cols-1 >= col+j >= 0:
                                if matrix[row+i][col+j] == 'x':
                                    matrix[row][col] += 1
            col += 1
        row += 1
    my_solution.append(my_data[0])
    my_solution.append(my_data[1])
    my_solution.append(matrix)
    return my_solution


# ------------------------------------------
# FUNCTION parse_out
# ------------------------------------------
def parse_out(output_name, my_solution):
    text = f"{my_solution[0]} {my_solution[1]}\n"
    for line in my_solution[2]:
        for i in line:
            text += f"{i} "
        text = text.rstrip()
        text += "\n"
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
    parse_out(output_name, my_solution)


# ---------------------------------------------------------------
#           PYTHON EXECUTION
# This is the main entry point to the execution of our program.
# It provides a call to the 'main function' defined in our
# Python program, making the Python interpreter to trigger
# its execution.
# ---------------------------------------------------------------
if __name__ == '__main__':
    # 1. Name of input and output files
    input_name = "input_2.txt"
    output_name = "output.txt"

    # 2. Main function
    my_main(input_name, output_name)

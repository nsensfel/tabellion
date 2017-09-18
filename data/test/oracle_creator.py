#!/usr/bin/env python3
import sys

solutions = list()

for line in sys.stdin.readlines():
    line_data = line.split(":")
    line_number = line_data[0]
    solution_number = int(line_data[2])
    solution_item_number = line_data[3].replace("$",'')

    if (len(solutions) <= solution_number):
        solutions.insert(solution_number, list())

    solutions[solution_number].insert(int(solution_item_number), line_number)

for sol in solutions:
    print("(" + (' '.join(sol)) + ")")

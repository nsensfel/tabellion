#!/usr/bin/env python3
import sys
import argparse


parser = argparse.ArgumentParser(
    description='Property to Predicate parser.'
)

parser.add_argument(
    "predicate_name",
    action='store',
    type=str,
    help="Name of the predicate."
)

parser.add_argument(
    "output_template_file",
    action='store',
    type=argparse.FileType(mode='w',encoding='UTF-8'),
    help="Predicate from Property template file."
)

parser.add_argument(
    "inferred_level",
    action='store',
    type=argparse.FileType(mode='a',encoding='UTF-8'),
    help="Inferred level file."
)

args = parser.parse_args()

var_names = list()
var_types = list()

for line in sys.stdin.readlines():
    line_data = line.strip().split(" ")
    print(line_data)
    var_name = line_data[0].strip()
    var_type = line_data[1].strip()

    args.inferred_level.write("(add_type " + var_type + ")\n")

    var_names.append(var_name)
    var_types.append(var_type)

args.inferred_level.write(
    "(add_predicate _"
    + args.predicate_name
    + " "
    + ' '.join(var_types)
    + ")\n"
)

params = ""
for i in range(len(var_types)):
    if (var_types[i] == "waveform"):
        new_id = ("$" + var_names[i] + ".WFM_ID$")
    else:
        new_id = ("$" + var_names[i] + ".ID$")

    params += (" " + new_id)

    args.output_template_file.write(
        "(add_element "
        + var_types[i]
        + " "
        + new_id
        + ")\n"
    )

args.output_template_file.write(
    "(_"
    + args.predicate_name
    + params
    + ")\n"
)


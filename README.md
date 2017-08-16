# Tabellion
Reports groups of elements from VHDL code according to logic formulas.

This can be used, for example, to check if coding rules have been followed, or
to confirm that a group of elements are indeed what their creator intended them
to be (flip-flops, state machines, etc...).

A mix of temporal (CTL) and first order logic (as well as a few special
operators) is used to write the formulas.

## Warning
The use of this tool for anything other than its development is currently not
recommended. Indeed, this is an early prototype: not only is it not feature
complete, but it is also likely to contain many bugs.

## Getting Started
Tabellion is composed of multiple sub-programs:
* A VHDL to AST translation program, not yet available, so GHDL is used instead.
* A model generator, ``ast-to-instr``, which populates predicates according to
the AST.
* Solvers, which handle the validation of properties for the aforementioned
model, ``instr-to-kodkod`` is currently the only solver available.
* Solution printers, which convert the results of the solver to a specific
format (sol-pretty-printer is the only one available at the moment).

### Prerequisites
* GHDL
* Java
* make

Additionally, ``instr-to-kodkod`` uses Kodkod, ANTLR, and Sat4j. Those will be
downloaded automatically by ``instr-to-kodkod`` and kept into its folder.

### How to Use
1. Clone the repository.
2. Use ``make`` to automatically fetch missing jars, compile everything and
launch the current configuration (which is found in the ``Makefile``). The use
of parallel processing is strongly recommended (e.g. ``make -j14`` to run 14
parallel jobs).

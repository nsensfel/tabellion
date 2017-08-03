# Tabellion
Reports groups of elements from the VHDL code according to logic formulas.

This can be used, for example, to check if coding rules have been followed, or
to confirm that a group of elements are indeed what their creator intended them
to be (flip-flops, state machines, etc...).

A mix of temporal (CTL) and first order logic (as well as a few special
operators) is used to write the formulas.

## Warning
The use of this tool for anything other than its development is currently not
recommended, as this is an early prototype and it likely contains many bugs.

## Getting Started
Here is a small guide on how to get the software running.

Tabellion is composed of multiple sub-programs:
* A VHDL to AST translation program, not yet available, so GHDL is used instead.
* A model generator, ``ast-to-instr``, which populates predicates according to
the AST.
* Solvers, which handle the validation of properties for the aforementioned
model, ``instr-to-kodkod`` is currently the only solver available.
* Solution printers, which convert the results of the solver to a specific
format (none available yet).

### Prerequisites
* GHDL
* Java
* make

### How to Use
1. Clone the repository.
3. Use ``make run`` to automatically fetch missing jars, compile everything and
launch the current configuration (which is found in the ``Makefile``). The use
of parallel processing is recommended (e.g. ``make run -j14``).

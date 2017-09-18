TABELLION_MAIN ?= $(shell pwd)/../../
AST_CREATOR = ghdl --file-to-xml
TEST_DIRS ?= $(addprefix $(shell pwd)/,$(wildcard */))
PROPERTY_DIR ?= $(shell pwd)/../property
SOLUTION_DIR ?= /tmp/tabellion/sol/
ORACLE_CREATOR_SCRIPT = $(shell pwd)/oracle_creator.py
################################################################################
TEST_FILES = \
	$(addsuffix /valid,$(TEST_DIRS)) \
	$(addsuffix /invalid,$(TEST_DIRS))

#VHD_FILES = $(addsuffix .vhd, $(TEST_FILES))
AST_FILES = $(addsuffix .xml, $(TEST_FILES))
OCL_FILES = $(addsuffix .ocl, $(TEST_FILES))
SOL_FILES = $(addsuffix .sol, $(TEST_FILES))

all: $(AST_FILES) $(OCL_FILES)

$(AST_FILES): %.xml : %.vhd
	$(AST_CREATOR) $< > $@

$(OCL_FILES): %.ocl : %.vhd
	grep -no "\$$SOL:[0-9]\+:[0-9]\+\\$$" $< | $(ORACLE_CREATOR_SCRIPT) > $@

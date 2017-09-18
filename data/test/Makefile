TABELLION_MAIN ?= ${CURDIR}/../../
AST_CREATOR = ghdl --file-to-xml
#TEST_DIRS ?= $(addprefix ${CURDIR}/,$(wildcard */))
TEST_DIRS ?= $(patsubst %/,%,$(wildcard */))
PROPERTY_DIR ?= ${CURDIR}/../property
SOLUTION_DIR ?= /tmp/tabellion/sol/
ORACLE_CREATOR_SCRIPT = ${CURDIR}/oracle_creator.py
################################################################################
TEST_FILES = $(addsuffix /valid,$(TEST_DIRS))

#VHD_FILES = $(addsuffix .vhd, $(TEST_FILES))
AST_FILES = $(addsuffix .xml, $(TEST_FILES))
OCL_FILES = $(addsuffix .ocl, $(TEST_FILES))

all: $(AST_FILES) $(OCL_FILES)
	for TD in $(TEST_DIRS) ; do \
		$(MAKE) -C $$TD PROPERTY_FILES=$$TD.pro
		

clean:
	rm -f $(AST_FILES)
	rm -f $(OCL_FILES)

$(AST_FILES): %.xml : %.vhd
	$(AST_CREATOR) $< > $@

$(OCL_FILES): %.ocl : %.vhd
	grep -no "\$$SOL:[0-9]\+:[0-9]\+\\$$" $< | $(ORACLE_CREATOR_SCRIPT) > $@



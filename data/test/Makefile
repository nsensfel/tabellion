TABELLION_MAIN ?= ${CURDIR}/../../
AST_CREATOR = ghdl --file-to-xml
#TEST_DIRS ?= $(addprefix ${CURDIR}/,$(wildcard */))
TEST_DIRS ?= $(patsubst %/,%,$(wildcard */))
PROPERTY_DIR ?= ${CURDIR}/../property
SOLUTION_DIR ?= /tmp/tabellion/sol/
ORACLE_CREATOR_SCRIPT = ${CURDIR}/oracle_creator.py
################################################################################
TEST_FILES = $(addsuffix /valid,$(TEST_DIRS)) $(addsuffix /invalid,$(TEST_DIRS))
SOLUTION_FILES = $(addsuffix .sol,$(addprefix $(SOLUTION_DIR)/,$(TEST_DIRS)))

#VHD_FILES = $(addsuffix .vhd, $(TEST_FILES))
AST_FILES = $(addsuffix .xml, $(TEST_FILES))
OCL_FILES = $(addsuffix .ocl, $(TEST_FILES))
VLD_FILES = $(addsuffix /valid.result, $(TEST_DIRS))
IVLD_FILES = $(addsuffix /invalid.result, $(TEST_DIRS))

export

all: $(AST_FILES) $(OCL_FILES) $(VLD_FILES) $(IVLD_FILES)

clean:
	rm -f $(AST_FILES)
	rm -f $(OCL_FILES)

$(AST_FILES): %.xml : %.vhd
	$(AST_CREATOR) $< > $@

$(OCL_FILES): %.ocl : %.vhd
	grep -no "\$$SOL:[0-9]\+:[0-9]\+\\$$" $< | $(ORACLE_CREATOR_SCRIPT) | sort > $@

$(VLD_FILES): %/valid.result: %/valid.ocl %/valid.xml
	$(MAKE) -C $(TABELLION_MAIN) \
		TMP_DIR=/tmp/tabellion_valid \
		clean_solutions
	$(MAKE) -C $(TABELLION_MAIN) \
		TMP_DIR=/tmp/tabellion_valid \
		clean_model
	$(MAKE) -C $(TABELLION_MAIN) \
		TMP_DIR=/tmp/tabellion_valid \
		AST_FILE=${PWD}/$(dir $@)/valid.xml \
		PROPERTY_FILES=$(PROPERTY_DIR)/$(patsubst %/,%,$(dir $@)).pro \
		TEMPLATE_DIR=${PWD}/$(dir $@)/ \
		NICE_MESSAGE=/tmp/tabellion/valid_test_result
	cat /tmp/tabellion/valid_test_result | sed '/^\s*$$/d' | sort > $@
	diff $@ $(dir $@)/valid.ocl || true > $@

$(IVLD_FILES): %/invalid.result: %/invalid.ocl %/invalid.xml
	$(MAKE) -C $(TABELLION_MAIN) \
		TMP_DIR=/tmp/tabellion_invalid \
		clean_solutions
	$(MAKE) -C $(TABELLION_MAIN) \
		TMP_DIR=/tmp/tabellion_invalid \
		clean_model
	$(MAKE) -C $(TABELLION_MAIN) \
		TMP_DIR=/tmp/tabellion_invalid \
		AST_FILE=${PWD}/$(dir $@)/invalid.xml \
		PROPERTY_FILES=$(PROPERTY_DIR)/$(patsubst %/,%,$(dir $@)).pro \
		TEMPLATE_DIR=${PWD}/$(dir $@)/ \
		NICE_MESSAGE=${PWD}/$@
	cat $@ | sed '/^\s*$$/d' | sort > $@

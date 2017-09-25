TABELLION_MAIN ?= ${CURDIR}/../../
AST_CREATOR = ghdl --file-to-xml
#TEST_DIRS ?= $(addprefix ${CURDIR}/,$(wildcard */))
TEST_DIRS ?= $(patsubst %/,%,$(wildcard */))
PROPERTIES_DIR ?= ${CURDIR}/../property
# TODO: Start using those variables...
SOLUTION_DIR ?= /tmp/tabellion/sol/
ORACLE_CREATOR_SCRIPT = ${CURDIR}/oracle_creator.py
################################################################################
TEST_FILES = $(addsuffix /valid,$(TEST_DIRS)) $(addsuffix /invalid,$(TEST_DIRS))
SOLUTION_FILES = $(addsuffix .sol,$(addprefix $(SOLUTION_DIR)/,$(TEST_DIRS)))

#VHD_FILES = $(addsuffix .vhd, $(TEST_FILES))
AST_FILES = $(addsuffix .xml, $(TEST_FILES))
OCL_FILES = $(addsuffix .ocl, $(TEST_FILES))
SOL_FILES = $(addsuffix .sol, $(TEST_FILES))
VLD_FILES = $(addsuffix /valid.result, $(TEST_DIRS))
IVLD_FILES = $(addsuffix /invalid.result, $(TEST_DIRS))

export

all: $(AST_FILES) $(OCL_FILES) $(VLD_FILES) $(IVLD_FILES)
	@echo "################################################################"
	@echo "## FALSE POSITIVES (should not have detected) ##################"
	@echo "################################################################"

	@for dir in $(TEST_DIRS) ; do \
		echo "#### $$dir ####"; \
		cat $$dir/invalid.result; \
	done

	@echo "################################################################"
	@echo "## FALSE NEGATIVES (should have detected) ######################"
	@echo "################################################################"

	@for dir in $(TEST_DIRS) ; do \
		echo "#### $$dir ####"; \
		cat $$dir/valid.result; \
	done

clean:
	rm -f $(AST_FILES)
	rm -f $(OCL_FILES)
	rm -f $(VLD_FILES)
	rm -f $(IVLD_FILES)
	rm -f $(SOL_FILES)
	rm -f $(addprefix .ready,$(SOL_FILES))
	rm -rf /tmp/tabellion_{,in}valid

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
		PROPERTIES=$(patsubst %/,%,$(dir $@)) \
		TEMPLATE_DIR=${PWD}/$(dir $@)/ \
		NICE_MESSAGE=/tmp/tabellion_valid/result
	cat /tmp/tabellion_valid/result | sed '/^\s*$$/d' | sort > /tmp/tabellion_valid/result_clean
	-diff /tmp/tabellion_valid/result_clean $(dir $@)/valid.ocl > $@

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
		PROPERTIES=$(patsubst %/,%,$(dir $@)) \
		TEMPLATE_DIR=${PWD}/$(dir $@)/ \
		NICE_MESSAGE=/tmp/tabellion_invalid/result
	cat /tmp/tabellion_invalid/result | sed '/^\s*$$/d' | sort > $@

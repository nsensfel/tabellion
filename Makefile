## Makefile Parameters #########################################################
LEVELS_DIR ?= ${CURDIR}/data/level/
PROPERTIES_DIR ?= ${CURDIR}/data/property/
PROPERTIES ?= $(basename $(notdir $(wildcard $(PROPERTIES_DIR)/*.pro)))
AST_FILE ?= ${CURDIR}/data/ast/best_chronometer_ever.xml
TEMPLATE_DIR ?= ${CURDIR}/data/template/
TO_PRED_TEMPLATE_DIR ?= ${CURDIR}/data/to_pred_template/
#AST_FILE = ${CURDIR}/data/ast/pong.xml
NICE_MESSAGE ?=

TMP_DIR ?= /tmp/tabellion
DEPENDENCIES_DIR ?= $(TMP_DIR)/deps
MODEL_DIR ?= $(TMP_DIR)/mod
MODEL_INSTANCES_DIR ?= $(MODEL_DIR)/instance
MODEL_INFERRED_DIR ?= $(MODEL_DIR)/inferred
SOL_DIR ?= $(TMP_DIR)/sol
INFERRED_LEVEL_FILE ?= $(LEVELS_DIR)/inferred.lvl

## Sub-programs ################################################################
AST_TO_INSTR ?= ast-to-instr
INST_CALC ?= instance-calculator
SOLVER ?= instr-to-kodkod
PRETTY_PRINTER ?= sol-pretty-printer
PROP_TO_PRED ?= prop_to_pred

export

################################################################################
ALL_PROPERTY_FILES = $(wildcard $(PROPERTIES_DIR)/*.pro)

ALL_DIRS = \
	$(TMP_DIR) \
	$(DEPENDENCIES_DIR) \
	$(MODEL_DIR) \
	$(MODEL_INSTANCES_DIR) \
	$(TO_PRED_TEMPLATE_DIR) \
	$(MODEL_INFERRED_DIR) \
	$(SOL_DIR)

all: $(ALL_DIRS)
	$(MAKE) compile
	$(MAKE) model
	$(MAKE) solutions

compile: $(ALL_DIRS)
	$(MAKE) -C $(AST_TO_INSTR) compile
	$(MAKE) -C $(INST_CALC) compile
	$(MAKE) -C $(SOLVER) compile
	$(MAKE) -C $(PRETTY_PRINTER) compile
	$(MAKE) -C $(PROP_TO_PRED) compile

model: $(ALL_DIRS)
	$(MAKE) -C $(AST_TO_INSTR) model
	$(MAKE) -C $(INST_CALC) model
	$(MAKE) -C $(SOLVER) model
	$(MAKE) -C $(PRETTY_PRINTER) model
	$(MAKE) -C $(PROP_TO_PRED) model

solutions: $(ALL_DIRS)
	$(MAKE) -C $(AST_TO_INSTR) solutions
	$(MAKE) -C $(INST_CALC) solutions
	$(MAKE) -C $(SOLVER) solutions
	$(MAKE) -C $(PRETTY_PRINTER) solutions
	$(MAKE) -C $(PROP_TO_PRED) solutions

clean:
	$(MAKE) -C $(AST_TO_INSTR) clean
	$(MAKE) -C $(INST_CALC) clean
	$(MAKE) -C $(SOLVER) clean
	$(MAKE) -C $(PRETTY_PRINTER) clean
	$(MAKE) -C $(PROP_TO_PRED) clean

clean_model:
	$(MAKE) -C $(AST_TO_INSTR) clean_model
	$(MAKE) -C $(INST_CALC) clean_model
	$(MAKE) -C $(SOLVER) clean_model
	$(MAKE) -C $(PRETTY_PRINTER) clean_model
	$(MAKE) -C $(PROP_TO_PRED) clean_model

clean_solutions:
	$(MAKE) -C $(AST_TO_INSTR) clean_solutions
	$(MAKE) -C $(INST_CALC) clean_solutions
	$(MAKE) -C $(SOLVER) clean_solutions
	$(MAKE) -C $(PRETTY_PRINTER) clean_solutions
	$(MAKE) -C $(PROP_TO_PRED) clean_solutions

$(ALL_DIRS):
	mkdir -p $@


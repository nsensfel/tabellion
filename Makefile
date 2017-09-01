## Makefile Parameters #########################################################
LEVEL_FILES = $(wildcard ${CURDIR}/data/level/*.lvl)
PROPERTY_FILES = \
	$(wildcard ${CURDIR}/data/property/test-case/*.pro)
#	$(wildcard ${CURDIR}/data/property/cnes/*.pro)
AST_FILE = ${CURDIR}/data/ast/best_chronometer_ever.xml
#AST_FILE = ${CURDIR}/data/ast/pong.xml

TMP_DIR = /tmp/tabellion
MODEL_DIR = $(TMP_DIR)/mod
MODEL_INSTANCES_DIR = $(MODEL_DIR)/instance
SOL_DIR = $(TMP_DIR)/sol

## Sub-programs ################################################################
AST_TO_INSTR = ast-to-instr
INST_CALC = instance-calculator
SOLVER = instr-to-kodkod
PRETTY_PRINTER = sol-pretty-printer

export

all: $(TMP_DIR) $(MODEL_DIR) $(SOL_DIR)
	$(MAKE) compile
	$(MAKE) model
	$(MAKE) solutions

compile:
	$(MAKE) -C $(AST_TO_INSTR) compile
	$(MAKE) -C $(INST_CALC) compile
	$(MAKE) -C $(SOLVER) compile
	$(MAKE) -C $(PRETTY_PRINTER) compile

model:
	$(MAKE) -C $(AST_TO_INSTR) model
	$(MAKE) -C $(INST_CALC) model
	$(MAKE) -C $(SOLVER) model
	$(MAKE) -C $(PRETTY_PRINTER) model

solutions: $(TMP_DIR) $(MODEL_DIR) $(SOL_DIR)
	$(MAKE) -C $(AST_TO_INSTR) solutions
	$(MAKE) -C $(INST_CALC) solutions
	$(MAKE) -C $(SOLVER) solutions
	$(MAKE) -C $(PRETTY_PRINTER) solutions

clean:
	$(MAKE) -C $(AST_TO_INSTR) clean
	$(MAKE) -C $(INST_CALC) clean
	$(MAKE) -C $(SOLVER) clean
	$(MAKE) -C $(PRETTY_PRINTER) clean

clean_model:
	$(MAKE) -C $(AST_TO_INSTR) clean_model
	$(MAKE) -C $(INST_CALC) clean_model
	$(MAKE) -C $(SOLVER) clean_model
	$(MAKE) -C $(PRETTY_PRINTER) clean_model

clean_solutions:
	$(MAKE) -C $(AST_TO_INSTR) clean_solutions
	$(MAKE) -C $(INST_CALC) clean_solutions
	$(MAKE) -C $(SOLVER) clean_solutions
	$(MAKE) -C $(PRETTY_PRINTER) clean_solutions

$(TMP_DIR):
	mkdir -p $@

$(MODEL_DIR):
	mkdir -p $@

$(SOL_DIR):
	mkdir -p $@

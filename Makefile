## Makefile Parameters #########################################################
LEVEL_FILES = $(wildcard $(PWD)/data/level/*.lvl)
PROPERTY_FILES = $(wildcard $(PWD)/data/property/*.pro)
AST_FILE = $(PWD)/data/ast/best_chronometer_ever.xml

TMP_DIR = /tmp/tabellion
MODEL_DIR = $(TMP_DIR)/mod
SOL_DIR = $(TMP_DIR)/sol

## Sub-programs ################################################################
AST_TO_INSTR = ast-to-instr
SOLVER = instr-to-kodkod
PRETTY_PRINTER = sol-pretty-printer

export

run: $(TMP_DIR) $(MODEL_DIR) $(SOL_DIR)
	$(MAKE) -C $(AST_TO_INSTR)
	$(MAKE) -C $(SOLVER)
	$(MAKE) -C $(PRETTY_PRINTER)

clean:
	$(MAKE) -C $(AST_TO_INSTR) clean
	$(MAKE) -C $(SOLVER) clean
	$(MAKE) -C $(PRETTY_PRINTER) clean

$(TMP_DIR):
	mkdir -p $@

$(MODEL_DIR):
	mkdir -p $@

$(SOL_DIR):
	mkdir -p $@

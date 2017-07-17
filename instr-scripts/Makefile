################################################################################
## LOCAL CONF ##################################################################
################################################################################
AST_FILE = ../data/ast/best_chronometer_ever.xml

PYTHON_EXEC = python3

## Structural Level
SL_INST_FILE = system.sl
SL_EXTRA_FILE = system_extra.sl

################################################################################
## PROGRAM FILES ###############################################################
################################################################################
UTIL_LIBS = id_manager.py waveform_manager.py process_internals.py
SL_LEVEL_GEN = structural_level.py

#EXTRA_SL_PARSER = sl_extra.py
################################################################################
GLOBAL_DEPS = $(UTIL_LIBS) $(AST_FILE)

$(SL_INST_FILE): $(SL_LEVEL_GEN) $(GLOBAL_DEPS)
	$(PYTHON_EXEC) $(SL_LEVEL_GEN) \
		-i $(AST_FILE) \
		-io $(SL_INST_FILE) \
		-eo $(SL_EXTRA_FILE)

export:
	scp \
		__init__.py Makefile \
		$(UTIL_LIBS) \
		$(SL_LEVEL_GEN) \
		dreamhost:~/noot-noot/onera_2017/scripts/

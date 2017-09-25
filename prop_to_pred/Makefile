## Parameters ##################################################################
TO_PRED_TEMPLATE_DIR ?=
INFERRED_LEVEL_FILE ?=
PARSING_SCRIPT ?= ${CURDIR}/parser.py
PROPERTIES_DIR ?=
#### Where to find the properties to verify
ALL_PROPERTY_FILES ?= $(wildcard $(PROPERTIES_DIR)/*.pro)

## Parameters Sanity Check #####################################################
ifeq ($(strip $(PARSING_SCRIPT)),)
$(error No PARSING_SCRIPT defined as parameter.)
endif

################################################################################

## Makefile Magic ##############################################################
PRED_TO_INFER = \
	$(addsuffix .pp,$(addprefix $(TO_PRED_TEMPLATE_DIR)/,$(notdir $(basename $(ALL_PROPERTY_FILES)))))
ADDITIONAL_MAKEFILES = \
	$(addsuffix .deps,$(addprefix $(DEPENDENCIES_DIR)/,$(basename $(notdir $(ALL_PROPERTY_FILES)))))

export
## Makefile Rules ##############################################################
compile: $(PRED_TO_INFER) $(ADDITIONAL_MAKEFILES)

model:

solutions:

clean:
	rm -f $(TO_PRED_TEMPLATE_DIR)/*
	rm -f $(INFERRED_LEVEL_FILE)

clean_model:

clean_solutions:
	rm -f $(TO_PRED_TEMPLATE_DIR)/*
	rm -f $(INFERRED_LEVEL_FILE)

########
$(TO_PRED_TEMPLATE_DIR)/%.pp: $(PROPERTIES_DIR)/%.pro
	cat $< | sed 's/.*;;.*//g' | tr -d "\n\r" \
		| sed -n 's/.*(tag_existing[ \t]\+([ \t]*\(\([ \t]*([^)]\+)\)\+\)[ \t]*.*)/\1/p' \
		| sed 's/)/)\n/g' | sed 's/[ \t]\+/ /g' | tr -d "()" \
		| $(PARSING_SCRIPT) \
			$(patsubst %.pp,%,$(notdir $(basename $@))) \
			$@ \
			$(INFERRED_LEVEL_FILE)
	$(MAKE) $(DEPENDENCIES_DIR)/$(patsubst %.pp,%,$(notdir $(basename $@))).deps

$(DEPENDENCIES_DIR)/%.deps: $(PROPERTIES_DIR)/%.pro
	printf "$(SOL_DIR)/$(notdir $(basename $@)).sol.ready: " > $@
	for dep in `sed -n 's/^#require \"\(.*\)\"$$/\1/p' $<` ; do \
		printf "$(SOL_DIR)/$$dep.sol " >> $@ ; \
	done
	printf "\n\t touch $(SOL_DIR)/$(notdir $(basename $@)).sol.ready\n\n" >> $@

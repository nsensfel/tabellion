## Parameters ##################################################################
#### Where to find the solutions.
SOL_DIR ?=

#### Where to find the model's data
MODEL_DIR ?=

#### Where to find the properties' pretty-print files
TEMPLATE_DIR ?=

#### Binaries
###### JRE binary
JAVA ?= java

###### JDK binary
JAVAC ?= javac

## Parameters Sanity Check #####################################################
ifeq ($(strip $(SOL_DIR)),)
$(error No SOL_DIR defined as parameter.)
endif

ifeq ($(strip $(MODEL_DIR)),)
$(error No MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(TEMPLATE_DIR)),)
$(error No TEMPLATE_DIR defined as parameter.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

################################################################################
CLASSPATH = "./src/"

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)

SOLUTION_FILES = $(wildcard $(SOL_DIR)/*.sol)
PROPERTY_PP_FILES = $(wildcard $(TEMPLATE_DIR)/*.pp)
SOLUTION_PP_PAIRS = \
	$(foreach sf,$(SOLUTION_FILES),$(sf) $(filter %$(basename $(notdir $(sf))).pp,$(PROPERTY_PP_FILES)))

## Makefile Rules ##############################################################
compile: $(CLASSES)

model:

solutions: $(SOLUTION_PP_PAIRS) $(MODEL_DIR)/structural.mod $(MODEL_DIR)/string_to_instr.map $(CLASSES)
	$(JAVA) -cp $(CLASSPATH) Main $(MODEL_DIR)/structural.mod $(MODEL_DIR)/string_to_instr.map $(SOLUTION_PP_PAIRS)

clean:
	rm -f $(CLASSES)
	rm -f $(SOL_DIR)/*.sol

clean_model:

clean_solutions:
	rm -f $(SOL_DIR)/*.sol

########
%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<


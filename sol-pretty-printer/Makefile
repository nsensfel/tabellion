## Parameters ##################################################################
#### Where to find the solutions.
ifndef SOL_DIR
SOL_DIR =
endif

#### Where to find the model's data
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Where to find the properties' pretty-print files
ifndef PROPERTY_FILES
PROPERTY_FILES =
endif

#### Binaries
###### JRE binary
ifndef JAVA
JAVA = java
endif

###### JDK binary
ifndef JAVAC
JAVAC = javac
endif

## Parameters Sanity Check #####################################################
ifeq ($(strip $(SOL_DIR)),)
$(error No SOL_DIR defined as parameter.)
endif

ifeq ($(strip $(MODEL_DIR)),)
$(error No MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(PROPERTY_FILES)),)
$(error No PROPERTY_FILES defined as parameter.)
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
PROPERTY_PP_FILES = $(PROPERTY_FILES:.pro=.pp)
SOLUTION_PP_PAIRS = \
	$(foreach sf,$(SOLUTION_FILES),"$(sf) $(filter %$(basename $(notdir $(sf))).pp,$(PROPERTY_PP_FILES))")

run: $(SOLUTION_PP_PAIRS) $(MODEL_DIR)/structural.mod $(MODEL_DIR)/string_to_instr.map $(CLASSES)
	$(JAVA) $(MODEL_DIR)/structural.mod $(MODEL_DIR)/string_to_instr.map $(SOLUTION_PP_PAIRS)

%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

clean:
	rm -f $(CLASSES)

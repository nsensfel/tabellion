## Parameters ##################################################################
#### Where to find the level files
ifndef LEVEL_FILES
LEVEL_FILES =
endif

#### Where to find the properties to verify
ifndef PROPERTY_FILES
PROPERTY_FILES =
endif

#### Where to find the model
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Where to store the CFG models
ifndef CFG_MODEL_DIR
CFG_MODEL_DIR = $(MODEL_DIR)/cfg/
endif

#### Where to output the solutions.
ifndef SOL_DIR
SOL_DIR =
endif

#### Where to get the missing Jar files.
ifndef JAR_SOURCE
JAR_SOURCE = "https://noot-noot.org/tabellion/jar/"
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

##### Downloader
ifndef DOWNLOADER
DOWNLOADER = wget
endif

## Parameters Sanity Check #####################################################
ifeq ($(strip $(LEVEL_FILES)),)
$(error No LEVEL_FILES defined as parameter.)
endif

ifeq ($(strip $(PROPERTY_FILES)),)
$(error No PROPERTY_FILES defined as parameter.)
endif

ifeq ($(strip $(MODEL_DIR)),)
$(error No MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(CFG_MODEL_DIR)),)
$(error No CFG_MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(SOL_DIR)),)
$(error No SOL_DIR defined as parameter.)
endif

ifeq ($(strip $(JAR_SOURCE)),)
$(error No JAR_SOURCE defined as parameter.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

ifeq ($(strip $(DOWNLOADER)),)
$(error No Java executable defined as parameter.)
endif

################################################################################
CLASSPATH = "kodkod.jar:./src/:./parser/:org.sat4j.core.jar:antlr-4.7-complete.jar"
REQUIRED_JARS = kodkod.jar org.sat4j.core.jar antlr-4.7-complete.jar

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)
SOLUTION_FILES = $(addprefix $(SOL_DIR)/,$(notdir $(PROPERTY_FILES:.pro=.sol)))
MODEL_FILES = \
	$(MODEL_DIR)/structural.mod \
	$(filter-out %structural.mod,$(wildcard $(MODEL_DIR)/*.mod))

export
## Makefile Rules ##############################################################
run: cfg-generator parser $(SOLUTION_FILES)

cfg-generator:
	$(MAKE) -C cfg-to-paths

parser: antlr-4.7-complete.jar
	$(MAKE) -C parser

clean:
	$(MAKE) -C parser clean
	$(MAKE) -C cfg-to-paths clean
	rm -f $(CLASSES)

%.sol: cfg-generator parser $(CLASSES) $(PROPERTY_FILES) $(MODEL_FILES) $(LEVEL_FILES)
	$(JAVA) -cp $(CLASSPATH) Main $@ \
		$(filter %$(basename $(notdir $@)).pro,$(PROPERTY_FILES)) \
		$(LEVEL_FILES) \
		$(MODEL_FILES)
		-v

%.class: %.java $(REQUIRED_JARS) parser
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'..."
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"

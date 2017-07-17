JAVAC = javac
JAVA = java
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)
CLASSPATH = "kodkod.jar:./src/:org.sat4j.core.jar"
TEST_FILE = "./system_minus_fun.sl"
DOWNLOADER = wget
JAR_SOURCE = https://noot-noot.org/onera_2017/jar/
REQUIRED_JARS = kodkod.jar org.sat4j.core.jar

all: $(CLASSES)

clean:
	rm -f $(CLASSES)

run: $(CLASSES) $(REQUIRED_JARS)
	$(JAVA) -cp $(CLASSPATH) Main $(TEST_FILE)

%.class: %.java $(REQUIRED_JARS)
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'"
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"


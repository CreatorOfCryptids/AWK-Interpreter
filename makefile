all: awk.class

awk.class: src/awk.java
	javac src/awk.java src/*/*.java src/*/*/*.java -d bin/

clean:
	rm ./bin/* -r
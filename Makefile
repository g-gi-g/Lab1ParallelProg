all: run

clean:
	rm -f out/SieveMain.jar out/SieveWorker.jar

out/SieveMain.jar: out/parcs.jar src/SieveMain.java src/Segment.java src/SieveWorker.java
	@javac -cp out/parcs.jar src/SieveMain.java src/Segment.java src/SieveWorker.java
	@jar cf out/SieveMain.jar -C src SieveMain.class -C src Segment.class
	@rm -f src/SieveMain.class src/Segment.class

out/SieveWorker.jar: out/parcs.jar src/SieveWorker.java src/Segment.java
	@javac -cp out/parcs.jar src/SieveWorker.java src/Segment.java
	@jar cf out/SieveWorker.jar -C src SieveWorker.class -C src Segment.class
	@rm -f src/SieveWorker.class src/Segment.class

build: out/SieveMain.jar out/SieveWorker.jar

run: out/SieveMain.jar out/SieveWorker.jar
	@cd out && java -cp 'parcs.jar:SieveMain.jar' SieveMain

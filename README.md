# ConceptualModelExtractor
This program extracts relations from user stories.

# Input
A text file containing user stories, one on each line. 

# Output
An excel file (output_cme.xlsx) containing extracted relations and the insights about reduction and coverage made. The file presents the data under the following columns:<br>
S.No.	<br>
User Story	<br>
Relations	<br>
#Relationships - Original Set	<br>
Relationships - Reduced Set 1	<br>
Relationships - Reduced Set 2	<br>
Reduction	# of tokens in user story	<br>
#of tokens covered by relations (excluding stopword tokens)	<br>
#of stopword tokens in user story	Coverage	Remarks <br>

# Dependencies
<b>Java:</b> openjdk version "17.0.3" 2022-04-19, OpenJDK Runtime Environment Temurin-17.0.3+7 (build 17.0.3+7) <br>
<b>StanfordCoreNLP:</b> stanford-corenlp-4.5.4 <br>
<b>ApachePOI:</b> poi-bin-5.2.3 <br>

# Executing the program
# Command Line
D:\Amol\docs\Softwares\Eclipse\eclipse\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_17.0.3.v20220515-1416\jre\bin\javaw.exe
-Dfile.encoding=UTF-8
-classpath "D:\Amol\docs\Projects\Java\ConceptualModelExtractor\bin;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\ejml-core-0.39.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\ejml-core-0.39-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\ejml-ddense-0.39.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\ejml-ddense-0.39-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\ejml-simple-0.39.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\ejml-simple-0.39-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\istack-commons-runtime-3.0.7.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\istack-commons-runtime-3.0.7-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\javax.activation-api-1.2.0.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\javax.activation-api-1.2.0-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\javax.json.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\javax.json-api-1.0-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\jaxb-api-2.4.0-b180830.0359.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\jaxb-api-2.4.0-b180830.0359-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\jaxb-impl-2.4.0-b180830.0438.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\jaxb-impl-2.4.0-b180830.0438-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\joda-time.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\joda-time-2.10.5-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\jollyday.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\jollyday-0.4.9-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\protobuf-java-3.19.6.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\slf4j-api.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\slf4j-simple.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\stanford-corenlp-4.5.4.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\stanford-corenlp-4.5.4-javadoc.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\stanford-corenlp-4.5.4-models.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\stanford-corenlp-4.5.4-sources.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\xom.jar;D:\Amol\docs\Softwares\StanfordCoreNLP\stanford-corenlp-4.5.4\xom-1.3.8-sources.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-ooxml-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-examples-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-excelant-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-javadoc-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-ooxml-full-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-ooxml-lite-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\poi-scratchpad-5.2.3.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\lib\commons-codec-1.15.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\lib\commons-collections4-4.4.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\lib\commons-io-2.11.0.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\lib\commons-math3-3.6.1.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\lib\log4j-api-2.18.0.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\lib\SparseBitSet-1.2.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\commons-compress-1.21.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\commons-logging-1.2.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\curvesapi-1.07.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\jakarta.activation-2.0.1.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\jakarta.xml.bind-api-3.0.1.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\slf4j-api-1.7.36.jar;D:\Amol\docs\Softwares\ApachePOI\poi-bin-5.2.3\ooxml-lib\xmlbeans-5.1.1.jar"
-XX:+ShowCodeDetailsInExceptionMessages CME_Main <USER_STORIES_FILE_NAME>.txt

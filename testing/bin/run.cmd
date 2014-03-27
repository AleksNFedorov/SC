@cd %~dp0..\


rmdir /Q /S "${environment.report.folder}"

java -Djava.io.tmpdir=${environment.report.folder} -cp ./conf;./lib/${project.artifactId}-${project.version}.${project.packaging} com.scejtesting.core.runner.ScejSuiteRunner ./conf/testsuite.xml

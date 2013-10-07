#!/usr/bin/env groovy

// test clrrepo-maven-plugin base functionality
GRP_ID='${project.groupId}'
ART_ID='${project.artifactId}'
VER='${project.version}'

ant = new AntBuilder()

try {
	// make test project
	ant.tempfile(property: 'art.dir', destDir: 'target')
	ant.echo(file: '${art.dir}/pom.xml', message: """
<project>
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.blogspot.cervus_alces</groupId>
	<artifactId>test_clean</artifactId>
	<packaging>jar</packaging>
	<version>1.0-SNAPSHOT</version>
	<build>
		<plugins>
			<plugin>
				<groupId>$GRP_ID</groupId>
				<artifactId>$ART_ID</artifactId>
				<version>$VER</version>
				<executions>
					<execution>
						<goals>
							<goal>purge</goal>
						</goals>
					</execution>
				</executions>
			</plugin>			
		</plugins>
	</build>
</project>
	""")

	for (skp in 1..0) {
		ant.exec dir: '${art.dir}', failonerror: true, command: "mvn -Dmaven.repo.local=repository -Dskip.repo.clean=${skp.asBoolean()} clean"
		assert ant.fileset(dir: '${art.dir}/repository', includes: "**/${ART_ID}-${VER}.jar").size() == skp, "UNEXPECTED ARTIFACT COUNT (must be $skp)"			
	}

} finally {
	// cleanup
	ant.delete dir: '${art.dir}'
}

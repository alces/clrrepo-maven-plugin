#!/usr/bin/env groovy

// test clrrepo-maven-plugin base functionality
GRP_ID='${project.groupId}'
ART_ID='${project.artifactId}'
VER='${project.version}'

// make test project
ant = new AntBuilder()
ant.tempfile(property: 'art.dir', destDir: System.properties['java.io.tmpdir'])
adir = ant.project.properties['art.dir']
ant.mkdir dir: adir

pom = new File(adir, 'pom.xml')

try {
	pom.write("""
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

	repo = new File(adir, 'repository')

	for (skp in [true, false]) {
		res = "mvn -f $pom.path -Dmaven.repo.local=$repo.path -Dskip.repo.clean=$skp clean".execute().text
		assert res.contains(' BUILD SUCCESS'), "MAVEN OUTPUT:\n$res"
		assert new File(repo, GRP_ID.replace('.', '/') + "/$ART_ID/$VER/${ART_ID}-${VER}.jar").exists() == skp, "ARTIFACT EXISTENSE ERROR"
	}
} finally {
	// cleanup
	ant.delete dir: adir
}

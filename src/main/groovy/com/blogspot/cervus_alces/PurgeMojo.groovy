package com.blogspot.cervus_alces

import org.codehaus.gmaven.mojo.GroovyMojo

/**
 * Cleans local maven repository
 *
 * @goal purge
 * @phase pre-clean
 */
class PurgeMojo extends GroovyMojo {

    /**
     * Path to local repository
     *
     * @parameter expression="${settings.localRepository}"
     *            default-value="~/.m2/repository"
     * @readonly		  
     */
    File repoPath

    /**
     * Skip actually clean repository?
     *
     * @parameter expression="${skip.repo.clean}"
     *            default-value=false		  
     */
    Boolean skip
   
	void execute() {
		log.debug "skip=$skip;repoPath=$repoPath"
		if (! skip) {
			log.info "purging $repoPath..."
			new AntBuilder().delete {
				fileset(dir: repoPath) {
					include name: "**/*.jar"
					include name: "**/*.pom"
					include name: "**/*.sha1"
				}
			}
		}
	}
}

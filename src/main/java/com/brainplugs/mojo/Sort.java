package com.brainplugs.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Collection;

/**
 * Mojo that sorts resource file keys
 */
@Mojo (name = "sort-keys", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true)
@Execute(phase = LifecyclePhase.COMPILE)
public class Sort extends AbstractMojo {

    @Parameter (property = "properties.root.dir", defaultValue = "${project.basedir}/src/main/resources", readonly = true)
    private File resourcesRoot;

    @Parameter (property = "properties.file.ext")
    private String[] fileExtensions;


    @Parameter( property = "properties.sort.skip", defaultValue = "false" )
    private boolean skip;

    private static final String[] defaultFileExtensions = {"props", "properties"};

    public void execute() throws MojoExecutionException, MojoFailureException {
        if(skip){
            getLog().info("Properties maven plugin execution skipped");
            return;
        }
        if(resourcesRoot.exists()) {
            getLog().info("Resource root specified as : " + resourcesRoot.getAbsolutePath());
            fileExtensions = (fileExtensions != null && fileExtensions.length > 0) ? fileExtensions : defaultFileExtensions;
            final Collection<File> files = FileUtils.listFiles(resourcesRoot, fileExtensions, true);
            files.forEach(this::readAndSort);
        }
    }

    private void readAndSort(final File file) {
        final SortedProperties properties = new SortedProperties(file, getLog());
        getLog().info("Writing sorted keys to file :" + file.getName());
        properties.store();
    }
}

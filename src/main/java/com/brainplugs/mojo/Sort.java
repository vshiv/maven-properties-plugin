package com.brainplugs.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Properties;

/**
 * Mojo that sorts resource file keys
 */
@Mojo (name = "sort-keys", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true)
public class Sort extends AbstractMojo {

    @Parameter (defaultValue = "${project.basedir}/src/main/resources", readonly = true)
    private File resourcesRoot;

    @Parameter
    private String[] fileExtensions;

    private static final String[] defaultFileExtensions = {"props", "properties"};

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Resource root specified as : " + resourcesRoot.getAbsolutePath());
        fileExtensions = (fileExtensions !=null && fileExtensions.length > 0) ? fileExtensions : defaultFileExtensions;
        final Collection<File> files = FileUtils.listFiles(resourcesRoot, fileExtensions, true);
        files.forEach( f -> {
                        getLog().info("Sort keys for file : " + f.getName());
                        readAndSort(f);
                      });
    }

    private void readAndSort(final File file) {
        InputStream input = null;
        OutputStream output = null;
        final Properties properties = new SortedProperties();
        try {
            input = new FileInputStream(file);
            properties.load(input);
            properties.entrySet().stream().forEach(entry -> getLog().debug("Key -> " + entry.getKey() + " Value -> " + entry.getValue() ));
            input.close();
            output = new FileOutputStream(file);
            properties.store(output, null);
            output.close();
        } catch (final IOException fe) {
            getLog().error(fe);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException ioe) {
                    getLog().error(ioe);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (final IOException ioe) {
                    getLog().error(ioe);
                }
            }
        }
    }
}

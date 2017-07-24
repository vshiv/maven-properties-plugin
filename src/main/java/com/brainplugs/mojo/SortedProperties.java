package com.brainplugs.mojo;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Class that extends {@link Properties} and overrides its key enumeration behavior
 */
class SortedProperties {

    private final File file;
    private final Log LOG;
    private final Map<String, CustomProperty> customProperties = new TreeMap<>();
    private String separator;

    SortedProperties(final File file, final Log log) {
        this.file = file;
        this.LOG = log;
        try {
            final PropertiesConfiguration.PropertiesReader reader = new PropertiesConfiguration.PropertiesReader(new FileReader(file));
            this.separator = reader.getPropertySeparator();
            while (reader.nextProperty()) {
                final CustomProperty property = new CustomProperty(reader.getPropertyName(), reader.getPropertyValue(), reader.getCommentLines());
                customProperties.putIfAbsent(property.key, property);
            }
            reader.close();
        } catch (final IOException e) {
            LOG.error("Error reading properties from file: " + file.getName(), e);
        }
    }

    private static class CustomProperty {

        private final String key;
        private final String value;
        private final List<String> comments;

        CustomProperty(final String key, final String value, final List<String> comments) {
            this.key = key;
            this.value = value;
            this.comments = new ArrayList<>(comments);
        }
    }

    void store() {
        try {
            final PropertiesConfiguration.PropertiesWriter writer = new PropertiesConfiguration.PropertiesWriter(new FileWriter(file), ',');
            writer.setCurrentSeparator(separator);
            for (final CustomProperty property : customProperties.values()) {
                property.comments.forEach((comment) -> {
                    try {
                        writer.writeln(comment);
                    } catch (final IOException e) {
                        LOG.error("Error writing comments to file: " + file.getName(), e);
                    }
                });
                writer.writeProperty(property.key, property.value);
            }
            writer.close();
        } catch (final IOException e) {
            LOG.error("Error writing properties to file : " + file.getName(), e);
        }
    }
}

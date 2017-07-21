package com.brainplugs.mojo;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Class that extends {@link Properties} and overrides its key enumeration behavior
 */
class SortedProperties extends Properties{

    public Enumeration<Object> keys() {
        final List<Object> keysList = Collections.list(super.keys());
        Collections.sort(keysList , (o1, o2 ) -> o1.toString().compareTo(o2.toString()));
        return Collections.enumeration(keysList);
    }


    boolean load(final File file) throws IOException {
        final FileInputStream inputStream = FileUtils.openInputStream(file);
        super.load(inputStream);
        inputStream.close();
        final List<String> lines = FileUtils.readLines(file);
        final Optional<String> first = lines.stream().findFirst();
        if(first.isPresent() && first.get().startsWith("#properties.maven.hash")){
            final String hashLine = first.get();
            final String[] lastHash = hashLine.split("=");
            return lastHash.length == 2 && !lastHash[1].equalsIgnoreCase("" + this.hashCode());
        }
        return true;
    }

    void store(final File file) throws IOException {
       final FileOutputStream fileOutputStream = new FileOutputStream(file);
       final PrintWriter writer = new PrintWriter(fileOutputStream);
       writer.println("#properties.maven.hash=" + hashCode());
       writer.flush();
       super.store(fileOutputStream, null);
       writer.close();
       fileOutputStream.close();
    }
}

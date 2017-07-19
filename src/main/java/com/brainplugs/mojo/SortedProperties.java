package com.brainplugs.mojo;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
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
}

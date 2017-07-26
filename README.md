# properties-maven-plugin
Maven plugin that sorts i18n keys in resource files and warns users about missing and unused keys.    
### Basic plugin usage
To sort keys just once add the plugin to your pom and run the following command
````
mvn properties:sort-keys
````
To run the plugin as part of your build lifecycle add the following to the plugins section of the pom.

````
    <build>
        <plugins>
            <plugin>
                <groupId>com.brainplugs</groupId>
                <artifactId>properties-maven-plugin</artifactId>
                <version>0.1-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>sort-keys</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
      </plugins>  
    </build>               
````
To skip plugin execution add a configuration section with skip set to true
````
           <plugin>
                <groupId>com.brainplugs</groupId>
                <artifactId>properties-maven-plugin</artifactId>
                <version>0.1-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>sort-keys</goal>
                        </goals>
                        <configuration>
                            <skip>true</skip>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
````
### Resources Root
Users can set the resources root directory by specifying the ````resourcesRoot```` property and specifying a valid file path to the directory. The default is ````${project.basedir}/src/main/resources````.    
### File extensions
Users can also set the file extensions of the resource files by specifying the ````fileExtensions```` property. The default file extensions supported are .props and .properties at present.    
     
The default life cycle phase for the sort-keys mojo in the properties-maven-plugin is compile. Of course this can be modified.  

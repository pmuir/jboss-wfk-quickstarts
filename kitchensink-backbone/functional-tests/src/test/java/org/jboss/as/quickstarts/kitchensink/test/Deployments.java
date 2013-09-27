package org.jboss.as.quickstarts.kitchensink.test;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;

public class Deployments {
    private static final String KITCHENSINK = "../target/jboss-kitchensink-backbone.war";

    public static WebArchive kitchensink() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(KITCHENSINK));
    }

}

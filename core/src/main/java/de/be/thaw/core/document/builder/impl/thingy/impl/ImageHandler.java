package de.be.thaw.core.document.builder.impl.thingy.impl;

import de.be.thaw.core.document.builder.impl.DocumentBuildContext;
import de.be.thaw.core.document.builder.impl.exception.DocumentBuildException;
import de.be.thaw.core.document.builder.impl.thingy.ThingyHandler;
import de.be.thaw.core.document.node.DocumentNode;
import de.be.thaw.text.model.tree.impl.ThingyNode;

import java.util.Set;

/**
 * Handler dealing with image thingies.
 */
public class ImageHandler implements ThingyHandler {

    /**
     * The default counter name to use for counting references.
     */
    private static final String DEFAULT_COUNTER_NAME = "image";

    @Override
    public Set<String> getThingyNames() {
        return Set.of("IMAGE");
    }

    @Override
    public void handle(ThingyNode thingyNode, DocumentNode documentNode, DocumentBuildContext ctx) throws DocumentBuildException {
        // Set the image reference counter (if the image got a caption or label -> is referencable).
        if (thingyNode.getOptions().containsKey("label") || thingyNode.getOptions().get("caption") != null) {
            String counterName = thingyNode.getOptions().getOrDefault("counter", DEFAULT_COUNTER_NAME);
            ctx.getReferenceModel().setReferenceNumber(counterName, documentNode.getId());
        }
    }

}


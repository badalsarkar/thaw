package de.be.thaw.typeset.knuthplass.converter.thingyhandler.impl;

import de.be.thaw.core.document.convert.exception.DocumentConversionException;
import de.be.thaw.core.document.node.DocumentNode;
import de.be.thaw.font.util.SuperScriptUtil;
import de.be.thaw.text.model.tree.impl.ThingyNode;
import de.be.thaw.typeset.knuthplass.config.util.FontDetailsSupplier;
import de.be.thaw.typeset.knuthplass.converter.context.ConversionContext;
import de.be.thaw.typeset.knuthplass.converter.thingyhandler.ThingyHandler;
import de.be.thaw.typeset.knuthplass.item.Item;
import de.be.thaw.typeset.knuthplass.item.impl.Glue;
import de.be.thaw.typeset.knuthplass.item.impl.box.FootNoteBox;
import de.be.thaw.typeset.knuthplass.paragraph.impl.TextParagraph;

import java.util.Set;

/**
 * Handler for dealing with foot notes.
 */
public class FootNoteHandler implements ThingyHandler {

    @Override
    public Set<String> getThingyNames() {
        return Set.of("FOOTNOTE");
    }

    @Override
    public void handle(ThingyNode node, DocumentNode documentNode, ConversionContext ctx) throws DocumentConversionException {
        if (!(ctx.getCurrentParagraph() instanceof TextParagraph)) {
            throw new DocumentConversionException(String.format(
                    "Expected the #FOOTNOTE# Thingy to be inside a text paragraph at %s",
                    node.getTextPosition()
            ));
        }

        // Increment foot note counter
        int counter = ctx.getDocument().getReferenceModel().setReferenceNumber("_FOOTNOTE", documentNode.getId());

        TextParagraph paragraph = (TextParagraph) ctx.getCurrentParagraph();

        // Remove leading white space (if any).
        Item lastItem = paragraph.items().get(paragraph.items().size() - 1);
        if (lastItem instanceof Glue) {
            paragraph.items().remove(paragraph.items().size() - 1);
        }

        DocumentNode footNoteDocumentNode = new DocumentNode("FOOTNOTE_NODE_" + counter, node, null, documentNode.getStyles());

        // Remap foot note document note to the new foot note dummy node
        DocumentNode footNoteContentNode = ctx.getDocument().getFootNotes().get(documentNode.getId());
        ctx.getDocument().getFootNotes().remove(documentNode.getId());
        ctx.getDocument().getFootNotes().put(footNoteDocumentNode.getId(), footNoteContentNode);

        // Append foot note number to current paragraph
        String value = SuperScriptUtil.getSuperScriptCharsForNumber(counter);
        FontDetailsSupplier.StringMetrics metrics;
        try {
            metrics = ctx.getConfig().getFontDetailsSupplier().measureString(footNoteDocumentNode, -1, value);
        } catch (Exception e) {
            throw new DocumentConversionException(e);
        }

        paragraph.addItem(new FootNoteBox(
                value,
                metrics,
                footNoteDocumentNode
        ));
    }

}

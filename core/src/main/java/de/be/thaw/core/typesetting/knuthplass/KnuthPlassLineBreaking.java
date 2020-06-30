package de.be.thaw.core.typesetting.knuthplass;

import de.be.thaw.core.document.Document;
import de.be.thaw.core.typesetting.knuthplass.config.LineBreakingConfig;
import de.be.thaw.core.typesetting.knuthplass.config.util.hyphen.HyphenatedWord;
import de.be.thaw.core.typesetting.knuthplass.config.util.hyphen.HyphenatedWordPart;
import de.be.thaw.core.typesetting.knuthplass.item.impl.Glue;
import de.be.thaw.core.typesetting.knuthplass.item.impl.Penalty;
import de.be.thaw.core.typesetting.knuthplass.item.impl.box.EmptyBox;
import de.be.thaw.core.typesetting.knuthplass.item.impl.box.TextBox;
import de.be.thaw.core.typesetting.knuthplass.paragraph.Paragraph;
import de.be.thaw.text.model.tree.Node;
import de.be.thaw.text.model.tree.NodeType;
import de.be.thaw.text.model.tree.impl.EnumerationItemNode;
import de.be.thaw.text.model.tree.impl.FormattedNode;
import de.be.thaw.text.model.tree.impl.TextNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Knuth-Plass line breaking algorithm.
 */
public class KnuthPlassLineBreaking {

    /**
     * Configuration of the
     */
    private final LineBreakingConfig config;

    /**
     * Paragraphs to type set.
     */
    private final List<Paragraph> paragraphs = new ArrayList<>();

    public KnuthPlassLineBreaking(LineBreakingConfig config) {
        this.config = config;
    }

    /**
     * Initialize the algorithm with the given document.
     *
     * @param document to typeset later
     */
    public void initialize(Document document) {
        paragraphs.clear();

        initializeForNode(document.getTextModel().getRoot());

        finalizeParagraph(); // Finalize the last paragraph
    }

    /**
     * Initialize the paragraphs for the given node.
     *
     * @param node to initialize for
     */
    private void initializeForNode(Node node) {
        switch (node.getType()) {
            case BOX, ENUMERATION -> initializeNewParagraph();
            case TEXT, FORMATTED -> initializeTextualNode(node);
            case ENUMERATION_ITEM -> initializeEnumerationItem((EnumerationItemNode) node);
        }

        // Process child nodes (if any)
        if (node.hasChildren()) {
            for (Node child : node.children()) {
                initializeForNode(child);
            }
        }
    }

    /**
     * Finalize the current paragraph.
     */
    private void finalizeParagraph() {
        if (paragraphs.isEmpty()) {
            return;
        }

        Paragraph current = paragraphs.get(paragraphs.size() - 1);

        // Add glue as stretchable space to fill the last line
        current.addItem(new Glue(0, Double.POSITIVE_INFINITY, 0));

        // Add explicit line break
        current.addItem(new Penalty(Double.NEGATIVE_INFINITY, 0, true));
    }

    /**
     * Initialize a new paragraph.
     */
    private void initializeNewParagraph() {
        if (!paragraphs.isEmpty()) {
            if (!paragraphs.get(paragraphs.size() - 1).isEmpty()) {
                finalizeParagraph();
            }
        }

        Paragraph paragraph = new Paragraph();
        paragraph.addItem(new EmptyBox(config.getFirstLineIndent()));

        paragraphs.add(paragraph);
    }

    /**
     * Initialize using a node that contains textual content.
     *
     * @param node to initialize with
     */
    private void initializeTextualNode(Node node) {
        String value;
        if (node.getType() == NodeType.TEXT) {
            value = ((TextNode) node).getValue();
        } else {
            value = ((FormattedNode) node).getValue();
        }

        // Find all words
        StringBuilder wordBuffer = new StringBuilder();
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);

            switch (c) {
                case ' ' -> {
                    appendWordToParagraph(wordBuffer.toString(), node);
                    wordBuffer.setLength(0); // Reset word buffer

                    // Add inter-word glue (representing a white space)
                    char lastChar = wordBuffer.length() > 0 ? wordBuffer.charAt(wordBuffer.length() - 1) : ' ';
                    paragraphs.get(paragraphs.size() - 1).addItem(new Glue(
                            config.getFontDetailsSupplier().getSpaceWidth(node),
                            config.getGlueConfig().getInterWordStretchability(lastChar),
                            config.getGlueConfig().getInterWordShrinkability(lastChar)
                    ));
                }
                case '-' -> {
                    wordBuffer.append(c);

                    // Add as word
                    appendWordToParagraph(wordBuffer.toString(), node);
                    wordBuffer.setLength(0); // Reset word buffer

                    // Add explicit hyphen to the paragraph
                    paragraphs.get(paragraphs.size() - 1).addItem(new Penalty(
                            config.getHyphenator().getExplicitHyphenPenalty(),
                            0,
                            true
                    ));
                }
                default -> wordBuffer.append(c);
            }
        }
    }

    /**
     * Append a word to the current paragraph.
     *
     * @param word to append
     * @param node the word belongs to
     */
    private void appendWordToParagraph(String word, Node node) {
        Paragraph paragraph = paragraphs.get(paragraphs.size() - 1);

        // Hyphenate word first
        HyphenatedWord hyphenatedWord = config.getHyphenator().hyphenate(word);
        List<HyphenatedWordPart> parts = hyphenatedWord.getParts();

        int len = parts.size();
        double hyphenWidth = len > 1 ? config.getFontDetailsSupplier().getCodeWidth(node, '-') : 0;

        for (int i = 0; i < len; i++) {
            HyphenatedWordPart part = parts.get(i);

            paragraph.addItem(new TextBox(
                    part.getPart(),
                    config.getFontDetailsSupplier().getStringWidth(node, part.getPart())
            ));

            boolean isLast = i == len - 1;
            if (!isLast) {
                // Add hyphen penalty to represent an optional hyphen
                paragraph.addItem(new Penalty(part.getPenalty(), hyphenWidth, true));
            }
        }
    }

    /**
     * Initialize an enumeration item.
     *
     * @param node to initialize with
     */
    private void initializeEnumerationItem(EnumerationItemNode node) {
        // TODO Special handling for an enumeration item where an explicit line break is added at the end and an enumeration item at the beginning
    }

}

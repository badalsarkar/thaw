package de.be.thaw.typeset.knuthplass.paragraph.handler.impl;

import de.be.thaw.style.model.StyleModel;
import de.be.thaw.style.model.block.StyleBlock;
import de.be.thaw.style.model.style.Style;
import de.be.thaw.style.model.style.StyleType;
import de.be.thaw.style.model.style.impl.InsetsStyle;
import de.be.thaw.style.model.style.impl.TextStyle;
import de.be.thaw.typeset.exception.TypeSettingException;
import de.be.thaw.typeset.knuthplass.TypeSettingContext;
import de.be.thaw.typeset.knuthplass.paragraph.Paragraph;
import de.be.thaw.typeset.knuthplass.paragraph.ParagraphType;
import de.be.thaw.typeset.knuthplass.paragraph.handler.ParagraphTypesetHandler;
import de.be.thaw.typeset.knuthplass.paragraph.impl.image.ImageParagraph;
import de.be.thaw.typeset.page.AbstractElement;
import de.be.thaw.typeset.page.Element;
import de.be.thaw.typeset.page.Page;
import de.be.thaw.typeset.page.impl.ImageElement;
import de.be.thaw.util.HorizontalAlignment;
import de.be.thaw.util.Position;
import de.be.thaw.util.Size;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler dealing with typesetting image paragraphs.
 */
public class ImageParagraphHandler implements ParagraphTypesetHandler {

    /**
     * The default caption prefix for figures.
     */
    private static final String DEFAULT_FIGURE_CAPTION_PREFIX = "Figure";

    @Override
    public ParagraphType supportedType() {
        return ParagraphType.IMAGE;
    }

    @Override
    public void handle(Paragraph paragraph, TypeSettingContext ctx) throws TypeSettingException {
        ImageParagraph imageParagraph = (ImageParagraph) paragraph;

        final InsetsStyle insetsStyle = paragraph.getNode().getStyle().getStyleAttribute(
                StyleType.INSETS,
                style -> Optional.ofNullable((InsetsStyle) style)
        ).orElseThrow();

        ctx.getPositionContext().increaseY(insetsStyle.getTop());

        double width = imageParagraph.getDefaultLineWidth() - (insetsStyle.getLeft() + insetsStyle.getRight());

        double ratio = imageParagraph.getSrc().getSize().getWidth() / imageParagraph.getSrc().getSize().getHeight();
        double height = width / ratio;

        double maxWidth = ctx.getConfig().getPageSize().getWidth() - (ctx.getConfig().getPageInsets().getLeft() + ctx.getConfig().getPageInsets().getRight()) - (insetsStyle.getLeft() + insetsStyle.getRight());
        double x = ctx.getConfig().getPageInsets().getLeft();
        if (imageParagraph.getAlignment() == HorizontalAlignment.CENTER) {
            x += (maxWidth - width) / 2;
        } else if (imageParagraph.getAlignment() == HorizontalAlignment.RIGHT) {
            x += maxWidth - width;
        }

        x += insetsStyle.getLeft();

        boolean isFloating = imageParagraph.isFloating() && imageParagraph.getAlignment() != HorizontalAlignment.CENTER;

        // Check if there is enough space for the image -> only when not floating!
        if (!isFloating) {
            double availableHeight = ctx.getAvailableHeight();
            if (availableHeight < height) {
                ctx.pushPage(); // Not enough space for this image on the current page -> create next page
            }
        }

        ImageElement imageElement = new ImageElement(
                imageParagraph.getSrc(),
                imageParagraph.getNode(),
                ctx.getCurrentPageNumber(),
                new Size(width, height),
                new Position(x, ctx.getPositionContext().getY())
        );
        ctx.pushPageElement(imageElement);

        double endY = ctx.getPositionContext().getY() + height + insetsStyle.getBottom();
        if (isFloating) {
            ctx.getFloatConfig().setFloatUntilY(endY);
            ctx.getFloatConfig().setFloatWidth(width + insetsStyle.getLeft() + insetsStyle.getRight());
            ctx.getFloatConfig().setFloatIndent(imageParagraph.getAlignment() == HorizontalAlignment.LEFT ? ctx.getFloatConfig().getFloatWidth() : 0);
        } else {
            ctx.getPositionContext().setY(endY);
        }

        // Deal with the image paragraphs caption (if any).
        if (imageParagraph.getCaption().isPresent()) {
            String caption = imageParagraph.getCaption().orElseThrow();
            addCaption(caption, imageParagraph, imageElement, insetsStyle, ctx);
        }
    }

    /**
     * Add the passed caption string under the image.
     *
     * @param caption      to add
     * @param paragraph    the image paragraph
     * @param imageElement the already typeset image element
     * @param insetsStyle  the insets of the image
     * @param ctx          the typesetting context
     * @throws TypeSettingException in case the caption could not be added properly
     */
    private void addCaption(String caption, ImageParagraph paragraph, ImageElement imageElement, InsetsStyle insetsStyle, TypeSettingContext ctx) throws TypeSettingException {
        boolean isFloating = paragraph.isFloating() && paragraph.getAlignment() != HorizontalAlignment.CENTER;
        double endY = imageElement.getPosition().getY() + imageElement.getSize().getHeight() + insetsStyle.getBottom();
        double x = imageElement.getPosition().getX();

        // Typeset the caption
        Map<StyleType, Style> styles = new HashMap<>();
        styles.put(StyleType.TEXT, new TextStyle(0.0, null, null, null, null, null, null, null));
        StyleBlock documentStyleBlock = new StyleBlock("DOCUMENT", styles);
        StyleModel styleModel = new StyleModel(new HashMap<>());
        styleModel.addBlock(documentStyleBlock.getName(), documentStyleBlock);

        List<Page> pages = ctx.typesetThawTextFormat(String.format(
                "**%s %d**: %s",
                paragraph.getCaptionPrefix() != null ? paragraph.getCaptionPrefix() : ctx.getConfig().getProperties().getOrDefault("image.caption.prefix", DEFAULT_FIGURE_CAPTION_PREFIX),
                ctx.getDocument().getReferenceModel().getReferenceNumber(paragraph.getNode().getId()),
                caption
        ), imageElement.getSize().getWidth(), styleModel);

        // Re-layout the elements below the image paragraph
        double maxY = endY;
        double startY = endY;
        for (Page page : pages) {
            for (Element element : page.getElements()) {
                double oldX = element.getPosition().getX();
                double oldY = element.getPosition().getY();

                // Set new position
                AbstractElement abstractElement = (AbstractElement) element;
                abstractElement.setPosition(new Position(
                        oldX + x,
                        oldY + endY
                ));

                // Check if new line
                if (element.getPosition().getY() + element.getSize().getHeight() > maxY) {
                    maxY = element.getPosition().getY() + element.getSize().getHeight();

                    // Check if there is enough space for the next line -> only when not floating
                    if (!isFloating) {
                        double captionHeight = maxY - startY;
                        double availableHeight = ctx.getAvailableHeight() - captionHeight;
                        if (availableHeight < element.getSize().getHeight()) {
                            ctx.pushPage(); // Create next page

                            startY = ctx.getConfig().getPageInsets().getTop();
                            endY = -oldY + startY;
                            maxY = startY + element.getSize().getHeight();

                            // Update current element position again
                            abstractElement.setPosition(new Position(
                                    oldX + x,
                                    oldY + endY
                            ));
                        }
                    }
                }

                ctx.pushPageElement(element);
            }
        }

        maxY += insetsStyle.getBottom();

        // Adjust floating configuration (if needed)
        if (isFloating) {
            ctx.getFloatConfig().setFloatUntilY(maxY);
        } else {
            ctx.getPositionContext().setY(maxY);
        }
    }

}

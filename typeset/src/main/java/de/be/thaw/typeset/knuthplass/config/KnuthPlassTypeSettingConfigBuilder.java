package de.be.thaw.typeset.knuthplass.config;

import de.be.thaw.font.ThawFont;
import de.be.thaw.text.parser.TextParser;
import de.be.thaw.typeset.knuthplass.config.util.FontDetailsSupplier;
import de.be.thaw.typeset.knuthplass.config.util.GlueConfig;
import de.be.thaw.typeset.knuthplass.config.util.hyphen.Hyphenator;
import de.be.thaw.typeset.knuthplass.config.util.image.ImageSourceSupplier;
import de.be.thaw.typeset.util.Insets;
import de.be.thaw.util.Size;

import java.io.File;
import java.util.Properties;

/**
 * Builder for the line breaking configuration.
 */
public class KnuthPlassTypeSettingConfigBuilder {

    /**
     * Default size of the de.be.thaw.typeset.page to typeset on (in arbitrary units).
     */
    private static final Size DEFAULT_PAGE_SIZE = new Size(210, 297);

    /**
     * The default page insets.
     */
    private static final Insets DEFAULT_INSETS = new Insets(30);

    /**
     * The default badness tolerance.
     * This means the maximum adjustment ratio allowed to fit a line.
     */
    private static final double DEFAULT_TOLERANCE = 1;

    /**
     * The default looseness setting.
     */
    private static final int DEFAULT_LOOSENESS = 0;

    /**
     * Default demerit added when the break points of two consecutive lines
     * are flagged.
     */
    private static final double DEFAULT_FLAGGED_DEMERIT = 100;

    /**
     * Default demerit added when two consecutive lines have different
     * fitness classes.
     */
    private static final double DEFAULT_FITNESS_DEMERIT = 100;

    /**
     * Size of the page to typeset on.
     */
    private Size pageSize = DEFAULT_PAGE_SIZE;

    /**
     * Insets of the pages.
     */
    private Insets pageInsets = DEFAULT_INSETS;

    /**
     * Tolerance of the badness.
     * This value is the maximum adjustment ratio allowed to fit a line.
     */
    private double tolerance = DEFAULT_TOLERANCE;

    /**
     * Defines how many lines more than the optimum value are allowed.
     */
    private int looseness = DEFAULT_LOOSENESS;

    /**
     * Demerit added when the break points of two consecutive lines
     * are flagged.
     */
    private double flaggedDemerit = DEFAULT_FLAGGED_DEMERIT;

    /**
     * Demerit added when two consecutive lines have different
     * fitness classes.
     */
    private double fitnessDemerit = DEFAULT_FITNESS_DEMERIT;

    /**
     * Width of indentation.
     */
    private double indentWidth = -1;

    /**
     * Supplier for font details.
     */
    private FontDetailsSupplier fontDetailsSupplier;

    /**
     * Hyphenator used to hyphenate words.
     */
    private Hyphenator hyphenator;

    /**
     * Configuration for the used glue.
     */
    private GlueConfig glueConfig;

    /**
     * The image source supplier to use.
     */
    private ImageSourceSupplier imageSourceSupplier;

    /**
     * Font to use for math expressions.
     */
    private ThawFont mathFont;

    /**
     * The working directory.
     */
    private File workingDirectory;

    /**
     * Text parser to use for parsing nested Thaw document text strings (for example captions).
     */
    private TextParser textParser;

    /**
     * Offset added to the page number.
     */
    private int pageNumberOffset = 0;

    /**
     * Properties containing needed translations during typesetting.
     */
    private Properties properties;

    /**
     * Whether headers and footers are allowed to be typeset.
     */
    private boolean allowHeadersAndFooters = true;

    /**
     * Get the size of the page to typeset on (in mm).
     *
     * @return size of the page
     */
    public Size getPageSize() {
        return pageSize;
    }

    /**
     * Set the page size of the de.be.thaw.typeset.page to typeset on (in mm).
     *
     * @param pageSize to set
     */
    public KnuthPlassTypeSettingConfigBuilder setPageSize(Size pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * Get the page insets.
     *
     * @return insets
     */
    public Insets getPageInsets() {
        return pageInsets;
    }

    /**
     * Set the page insets to use.
     *
     * @param pageInsets to set
     */
    public KnuthPlassTypeSettingConfigBuilder setPageInsets(Insets pageInsets) {
        this.pageInsets = pageInsets;

        return this;
    }

    /**
     * Get the supplier that is able to deliver details about a used font
     * needed to properly typeset a text.
     *
     * @return supplier
     */
    public FontDetailsSupplier getFontDetailsSupplier() {
        return fontDetailsSupplier;
    }

    /**
     * Set the font details supplier needed to request details about a used font.
     *
     * @param fontDetailsSupplier to set
     */
    public KnuthPlassTypeSettingConfigBuilder setFontDetailsSupplier(FontDetailsSupplier fontDetailsSupplier) {
        this.fontDetailsSupplier = fontDetailsSupplier;

        return this;
    }

    /**
     * Get the hyphenator used to hyphenate words.
     *
     * @return hyphenator
     */
    public Hyphenator getHyphenator() {
        return hyphenator;
    }

    /**
     * Set the hyphenator used to hyphenate words.
     *
     * @param hyphenator to set
     */
    public KnuthPlassTypeSettingConfigBuilder setHyphenator(Hyphenator hyphenator) {
        this.hyphenator = hyphenator;

        return this;
    }

    /**
     * Get the configuration for the used glue.
     *
     * @return glue config
     */
    public GlueConfig getGlueConfig() {
        return glueConfig;
    }

    /**
     * Set the configuration for the used glue.
     *
     * @param glueConfig to set
     */
    public KnuthPlassTypeSettingConfigBuilder setGlueConfig(GlueConfig glueConfig) {
        this.glueConfig = glueConfig;

        return this;
    }

    /**
     * Tolerance of the badness.
     * This value is the maximum adjustment ratio allowed to fit a line.
     *
     * @return tolerance
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Set the tolerance of the badness.
     * This value is the maximum adjustment ratio allowed to fit a line.
     *
     * @param tolerance to set
     */
    public KnuthPlassTypeSettingConfigBuilder setTolerance(double tolerance) {
        this.tolerance = tolerance;

        return this;
    }

    /**
     * Get how many lines more than the optimum value are allowed.
     *
     * @return looseness
     */
    public int getLooseness() {
        return looseness;
    }

    /**
     * Set how many lines more than the optimum value are allowed.
     *
     * @param looseness to set
     */
    public KnuthPlassTypeSettingConfigBuilder setLooseness(int looseness) {
        this.looseness = looseness;

        return this;
    }

    /**
     * Get the demerit added when the break points of two consecutive lines
     * are flagged.
     *
     * @return flagged demerit
     */
    public double getFlaggedDemerit() {
        return flaggedDemerit;
    }

    /**
     * Set the demerit added when break points of two consecutive lines
     * are flagged.
     *
     * @param flaggedDemerit to set
     */
    public KnuthPlassTypeSettingConfigBuilder setFlaggedDemerit(double flaggedDemerit) {
        this.flaggedDemerit = flaggedDemerit;

        return this;
    }

    /**
     * Get the demerit added when two consecutive lines have different
     * fitness classes.
     *
     * @return fitness demerit
     */
    public double getFitnessDemerit() {
        return fitnessDemerit;
    }

    /**
     * Set the demerit added when two consecutive lines have different
     * fitness classes.
     *
     * @param fitnessDemerit to set
     */
    public KnuthPlassTypeSettingConfigBuilder setFitnessDemerit(double fitnessDemerit) {
        this.fitnessDemerit = fitnessDemerit;

        return this;
    }

    /**
     * Get the width of indentation.
     *
     * @return indentation width
     */
    public double getIndentWidth() {
        if (indentWidth == -1) {
            return pageSize.getWidth() * 0.05;
        }

        return indentWidth;
    }

    /**
     * Set the width of indentation.
     *
     * @param indentWidth to set
     */
    public KnuthPlassTypeSettingConfigBuilder setIndentWidth(double indentWidth) {
        this.indentWidth = indentWidth;

        return this;
    }

    /**
     * Get the image source supplier.
     *
     * @return image source supplier
     */
    public ImageSourceSupplier getImageSourceSupplier() {
        return imageSourceSupplier;
    }

    /**
     * Get the image source supplier.
     *
     * @param imageSourceSupplier to set
     */
    public KnuthPlassTypeSettingConfigBuilder setImageSourceSupplier(ImageSourceSupplier imageSourceSupplier) {
        this.imageSourceSupplier = imageSourceSupplier;

        return this;
    }

    /**
     * Get the font to use for math expressions.
     *
     * @return math font
     */
    public ThawFont getMathFont() {
        return mathFont;
    }

    /**
     * Set the font to use for math expressions.
     *
     * @param mathFont to set
     * @return the builder instance
     */
    public KnuthPlassTypeSettingConfigBuilder setMathFont(ThawFont mathFont) {
        this.mathFont = mathFont;

        return this;
    }

    /**
     * Get the working directory.
     *
     * @return working directory
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Set the working directory.
     *
     * @param workingDirectory to set
     */
    public KnuthPlassTypeSettingConfigBuilder setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;

        return this;
    }

    /**
     * Get the text parser to use to parse for example captions for image paragraphs.
     *
     * @return text parser
     */
    public TextParser getTextParser() {
        return textParser;
    }

    /**
     * Set the text parser to use.
     *
     * @param textParser to set
     */
    public KnuthPlassTypeSettingConfigBuilder setTextParser(TextParser textParser) {
        this.textParser = textParser;

        return this;
    }

    /**
     * Get the offset added to the page number.
     *
     * @return page number offset
     */
    public int getPageNumberOffset() {
        return pageNumberOffset;
    }

    /**
     * Set the offset added to the page number.
     *
     * @param pageNumberOffset to set
     * @return the builder
     */
    public KnuthPlassTypeSettingConfigBuilder setPageNumberOffset(int pageNumberOffset) {
        this.pageNumberOffset = pageNumberOffset;

        return this;
    }

    /**
     * Get the properties containing translations.
     *
     * @return properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Set properties containing translations needed during typesetting.
     *
     * @param properties to set
     * @return the builder
     */
    public KnuthPlassTypeSettingConfigBuilder setProperties(Properties properties) {
        this.properties = properties;

        return this;
    }

    /**
     * Whether typesetting headers and footers is allowed.
     *
     * @return allow headers and footers
     */
    public boolean isAllowHeadersAndFooters() {
        return allowHeadersAndFooters;
    }

    /**
     * Set whether headers and footers are allowed to be typeset.
     *
     * @param allow whether to allow
     * @return the builder
     */
    public KnuthPlassTypeSettingConfigBuilder setAllowHeadersAndFooters(boolean allow) {
        this.allowHeadersAndFooters = allow;

        return this;
    }


    /**
     * Build the line breaking configuration.
     *
     * @return the build config
     */
    public KnuthPlassTypeSettingConfig build() {
        return new KnuthPlassTypeSettingConfig(
                getPageSize(),
                getPageInsets(),
                getIndentWidth(),
                getLooseness(),
                getTolerance(),
                getFlaggedDemerit(),
                getFitnessDemerit(),
                getFontDetailsSupplier(),
                getHyphenator(),
                getGlueConfig(),
                getImageSourceSupplier(),
                getMathFont(),
                getWorkingDirectory(),
                getTextParser(),
                getPageNumberOffset(),
                getProperties(),
                isAllowHeadersAndFooters()
        );
    }

}

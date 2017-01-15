/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wandrell.velocity.tool;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Pattern;

import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.generic.SafeConfig;
import org.apache.velocity.tools.generic.ValueParser;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Utilities class to ease using custom Maven Site configuration values through
 * Velocity.
 * <p>
 * The configuration values should be in the site.xml file, inside a {@code 
 * <skinConfig>}, itself inside the {@code <custom>} element.
 * <p>
 * Any value stored there can be acquired through the use of the
 * {@link #get(String) get} method.
 * <p>
 * If a {@code <pages>} element is defined, it can contain an element with a
 * page's id (which is the slugged name of the file) where it can override any
 * of those values.
 * <p>
 * Unlike other utilities classes in the project, this one is stateful, as it
 * binds itself to the context and data of the page being rendered.
 * <p>
 * This class has been created from the Skin Config Tool class from the
 * <a href="http://andriusvelykis.github.io/reflow-maven-skin/">Reflow Maven
 * Skin</a>.
 * 
 * @author Andrius Velykis
 * @author Bernardo Martínez Garrido
 */
@DefaultKey("config")
public final class SkinConfigUtils extends SafeConfig {

    /**
     * The key identifying the current file name in the velocity context.
     */
    public static final String CURRENT_FILE_NAME_KEY = "currentFileName";

    /**
     * The key identifying the decoration in the velocity context.
     */
    public static final String DECORATION_KEY        = "decoration";

    /**
     * The key identifying the Maven project.
     */
    public static final String MAVEN_PROJECT_KEY     = "project";

    /**
     * The key identifying the pages node.
     */
    public static final String PAGES_KEY             = "pages";

    /**
     * Key for the skin configuration.
     * <p>
     * This is the name of the node inside the site.xml file where the skin's
     * configuration is stored.
     * <p>
     * It will be a node inside the custom node, with the project node at the
     * root, like this:
     * 
     * <pre>
     * {@code <project>
     *   <custom>
     *      <skinConfig></skinConfig>
     *   </custom>
     * </project>}
     * </pre>
     * <p>
     * That is, if the default value of skinConfig is kept.
     */
    public static final String SKIN_KEY              = "skinConfig";

    /**
     * The key identifying the velocity context.
     */
    public static final String VELOCITY_CONTEXT_KEY  = "velocityContext";

    /**
     * Identifier for the current file.
     * <p>
     * This is a slug created from the current file's name.
     */
    private String             fileId;

    /**
     * Regex for multiple lines.
     */
    private final Pattern      multipleLine          = Pattern.compile("-+");

    /**
     * Regex for non-latin characters.
     */
    private final Pattern      nonLatin              = Pattern
            .compile("[^\\w-]");

    /**
     * Page configuration node.
     * <p>
     * This is the node for the current page inside the {@code <pages>} node,
     * located in the {@code <skinConfig>} node, in the site.xml file.
     */
    private Xpp3Dom            pageConfig            = new Xpp3Dom("");

    /**
     * Identifier for the project.
     * <p>
     * This is a slug created from the artifact id contained in the POM file.
     */
    private String             projectId;

    /**
     * Skin configuration node.
     * <p>
     * This is the {@code <skinConfig>} located in the site.xml file, inside the
     * {@code <custom>} node.
     */
    private Xpp3Dom            skinConfig            = new Xpp3Dom("");

    /**
     * Regex for whitespaces.
     */
    private final Pattern      whitespace            = Pattern.compile("[\\s]");

    /**
     * Constructs an instance of the {@code SkinConfigUtil}.
     */
    public SkinConfigUtils() {
        super();
    }

    /**
     * Returns a configuration's node property.
     * <p>
     * This will be the data on the site.xml file where the node is called like
     * the property.
     * <p>
     * Thanks to Velocity, instead of using {@code $config.get("myproperty")},
     * this method can be called as a getter by using {@code $config.myproperty}
     * .
     * <p>
     * The method will look for the property first in the page configuration. If
     * it is not found there, then it looks for it in the global configuration.
     * If again it is not found, then the {@code null} value is returned.
     * 
     * @param property
     *            the property being queried
     * @return the value assigned to the property in the page or the global
     *         properties
     */
    public final Xpp3Dom get(final String property) {
        Xpp3Dom value; // Node with the property's value

        checkNotNull(property, "Received a null pointer as property");

        // Looks for it in the page properties
        value = getPageConfig().getChild(property);

        if (value == null) {
            // It was not found in the page properties
            // New attempt with the global properties
            value = getSkinConfig().getChild(property);
        }

        return value;
    }

    /**
     * Returns the file identifier.
     * <p>
     * This is the slugged current file name.
     * <p>
     * It can be called through Velocity with the command {@code $config.fileId}
     * .
     * 
     * @return the file identifier
     */
    public final String getFileId() {
        return fileId;
    }

    /**
     * Returns the project identifier.
     * <p>
     * This is the slugged artifact id from the POM file.
     * <p>
     * It can be called through Velocity with the command
     * {@code $config.projectId}.
     * 
     * @return the project id
     */
    public final String getProjectId() {
        return projectId;
    }

    /**
     * Returns the boolean value of a property's value.
     * <p>
     * This will transform whatever value the property has assigned to a
     * boolean.
     * 
     * @param property
     *            the property to check
     * @return the property's value transformed to a boolean
     */
    public final Boolean isTrue(final String property) {
        final Xpp3Dom value; // Node with the property's value
        final Boolean result; // Value transformed to a boolean

        checkNotNull(property, "Received a null pointer as property");

        value = get(property);

        if (value == null) {
            result = false;
        } else {
            result = Boolean.valueOf(value.getValue());
        }

        return result;
    }

    /**
     * Returns the regular expression for multiple lines.
     * 
     * @return the regular expression for multiple lines
     */
    private final Pattern getMultipleLinePattern() {
        return multipleLine;
    }

    /**
     * Returns the non-latin characters regular expression.
     * 
     * @return the non-latin characters regular expression
     */
    private final Pattern getNonLatinPattern() {
        return nonLatin;
    }

    /**
     * Returns the page configuration node.
     * 
     * @return the page configuration node
     */
    private final Xpp3Dom getPageConfig() {
        return pageConfig;
    }

    /**
     * Returns the skin config node.
     * 
     * @return the skin config node
     */
    private final Xpp3Dom getSkinConfig() {
        return skinConfig;
    }

    /**
     * Returns the regular expression for whitespaces.
     * 
     * @return the regular expression for whitespaces
     */
    private final Pattern getWhitespacePattern() {
        return whitespace;
    }

    /**
     * Loads the file identifier from the velocity tools context.
     * <p>
     * This is generated from the file's name.
     * 
     * @param context
     *            the Velocity tools context
     */
    private final void loadFileId(final ToolContext context) {
        final Integer lastDot; // Location of the extension dot
        final Object currentFileObj; // File's name as received
        String currentFile; // File's name

        if (context.containsKey(CURRENT_FILE_NAME_KEY)) {
            currentFileObj = context.get(CURRENT_FILE_NAME_KEY);
            if (currentFileObj == null) {
                setFileId("");
            } else {
                currentFile = String.valueOf(currentFileObj);

                // Drops the extension
                lastDot = currentFile.lastIndexOf('.');
                if (lastDot >= 0) {
                    currentFile = currentFile.substring(0, lastDot);
                }

                // File name is slugged
                setFileId(slug(currentFile));
            }
        } else {
            setFileId("");
        }
    }

    /**
     * Loads the project identifier from the velocity tools context.
     * <p>
     * This is generated from the artifact id on the POM file.
     * 
     * @param context
     *            the Velocity tools context
     */
    private final void loadProjectId(final ToolContext context) {
        final Object projectObj; // Object with the project info
        final MavenProject project; // Casted project info
        final String artifactId; // Maven artifact id

        if (context.containsKey(MAVEN_PROJECT_KEY)) {
            projectObj = context.get(MAVEN_PROJECT_KEY);
            if (projectObj instanceof MavenProject) {
                project = (MavenProject) projectObj;
                artifactId = project.getArtifactId();
                if (artifactId != null) {
                    // The artifact id is slugged for the project id
                    setProjectId(slug(artifactId));
                } else {
                    setProjectId("");
                }
            } else {
                setProjectId("");
            }
        } else {
            setProjectId("");
        }
    }

    /**
     * Processes the decoration model, acquiring the skin and page
     * configuration.
     * <p>
     * The decoration model are the contents of the site.xml file.
     * 
     * @param model
     *            decoration data
     */
    private final void processDecoration(final DecorationModel model) {
        final Object customObj; // Object for the <custom> node
        final Xpp3Dom customNode; // <custom> node
        final Xpp3Dom pagesNode; // <pages> node
        final Xpp3Dom skinNode; // <skinConfig> node
        final Xpp3Dom page; // Current page node

        customObj = model.getCustom();

        if (customObj instanceof Xpp3Dom) {
            // This is the <custom> node in the site.xml file

            customNode = (Xpp3Dom) customObj;

            // Acquires <skinConfig> node
            skinNode = customNode.getChild(SKIN_KEY);

            checkNotNull(skinNode,
                    "The skin configuration node is missing from the decoration. Make sure it can be found in the <custom> node, inside the site.xml file");

            setSkinConfig(skinNode);

            // Acquires the <pages> node
            pagesNode = skinNode.getChild(PAGES_KEY);
            if (pagesNode != null) {

                // Get the page node for the current file
                page = pagesNode.getChild(getFileId());

                if (page != null) {
                    setPageConfig(page);
                }
            }
        }
    }

    /**
     * Sets the identifier for the current file.
     * 
     * @param id
     *            the identifier for the current file
     */
    private final void setFileId(final String id) {
        fileId = id;
    }

    /**
     * Sets the page configuration node.
     * 
     * @param config
     *            the page configuration node
     */
    private final void setPageConfig(final Xpp3Dom config) {
        pageConfig = config;
    }

    /**
     * Sets the project identifier.
     * 
     * @param id
     *            the project identifier
     */
    private final void setProjectId(final String id) {
        projectId = id;
    }

    /**
     * Sets the skin config node.
     * 
     * @param config
     *            the skin config node.
     */
    private final void setSkinConfig(final Xpp3Dom config) {
        skinConfig = config;
    }

    /**
     * Returns a URL slug created from the received text.
     * <p>
     * A slug is a human-readable version of the text, where all the special
     * characters have been removed, and spaces have been swapped by dashes.
     * <p>
     * For example: <em>This, That & the Other! Various Outré
     * Considerations</em> would become
     * <em>this-that-the-other-various-outre-considerations</em>
     * <p>
     * Of course, this can be applied to any text, not just URLs, but it is
     * usually used in the context of an URL.
     * 
     * @param text
     *            text to generate the slug from
     * @return the slug of the given text
     */
    private final String slug(final String text) {
        final String separator; // Separator for swapping whitespaces
        String corrected; // Modified string

        checkNotNull(text, "Received a null pointer as the text");

        separator = "-";

        corrected = text.replace('/', '-').replace('\\', '-').replace('.', '-')
                .replace('_', '-');

        // Removes multiple lines
        corrected = getMultipleLinePattern().matcher(corrected)
                .replaceAll(separator);
        // Removes white spaces
        corrected = getWhitespacePattern().matcher(corrected)
                .replaceAll(separator);
        // Removes non-latin characters
        corrected = getNonLatinPattern().matcher(corrected).replaceAll("");

        return corrected.toLowerCase();
    }

    @Override
    protected final void configure(final ValueParser values) {
        final Object velocityContext; // Value from the parser
        final ToolContext ctxt; // Casted context
        final Object decorationObj; // Value of the decoration key

        checkNotNull(values, "Received a null pointer as values");

        velocityContext = values.get(VELOCITY_CONTEXT_KEY);

        if (velocityContext instanceof ToolContext) {
            ctxt = (ToolContext) velocityContext;

            loadProjectId(ctxt);

            loadFileId(ctxt);

            decorationObj = ctxt.get(DECORATION_KEY);
            if (decorationObj instanceof DecorationModel) {
                processDecoration((DecorationModel) decorationObj);
            }
        }
    }

}

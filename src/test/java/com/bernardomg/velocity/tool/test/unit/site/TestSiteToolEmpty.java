/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015-2019 the original author or authors.
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

package com.bernardomg.velocity.tool.test.unit.site;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.bernardomg.velocity.tool.SiteTool;

/**
 * Unit tests for {@link SiteTool}, testing the methods using empty strings.
 * <p>
 * The meaning behind this test is verifying that the initial queries done by
 * the utilities class doesn't break with empty inputs.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 * @see SiteTool
 */
@RunWith(JUnitPlatform.class)
public final class TestSiteToolEmpty {

    /**
     * Instance of the utils class being tested.
     */
    private final SiteTool util = new SiteTool();

    /**
     * Default constructor.
     */
    public TestSiteToolEmpty() {
        super();
    }

    /**
     * Tests that an empty string causes no problem.
     */
    @Test
    public final void testFixAnchorLinks_EmptyString() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "";
        htmlExpected = "";

        element = Jsoup.parse(html).body();
        util.fixAnchorLinks(element);

        Assertions.assertEquals(htmlExpected, element.html());
    }

    /**
     * Tests that an empty string causes no problem.
     */
    @Test
    public final void testFixHeadingIds_EmptyString() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "";
        htmlExpected = "";

        element = Jsoup.parse(html).body();
        util.fixHeadingIds(element);

        Assertions.assertEquals(htmlExpected, element.html());
    }

    /**
     * Tests that an empty string causes no problem.
     */
    @Test
    public final void testFixReport_EmptyString() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "";
        htmlExpected = "";

        element = Jsoup.parse(html).body();
        util.fixReport(element, "");

        Assertions.assertEquals(htmlExpected, element.html());
    }

    /**
     * Tests that an empty string causes no problem.
     */
    @Test
    public final void testTransformIcons_EmptyString() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "";
        htmlExpected = "";

        element = Jsoup.parse(html).body();
        util.transformIcons(element);

        Assertions.assertEquals(htmlExpected, element.html());
    }

    /**
     * Tests that an empty string causes no problem.
     */
    @Test
    public final void testTransformImagesToFigures_EmptyString() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "";
        htmlExpected = "";

        element = Jsoup.parse(html).body();
        util.transformImagesToFigures(element);

        Assertions.assertEquals(htmlExpected, element.html());
    }

    /**
     * Tests that an empty string causes no problem.
     */
    @Test
    public final void testTransformTables_EmptyString() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "";
        htmlExpected = "";

        element = Jsoup.parse(html).body();
        util.transformTables(element);

        Assertions.assertEquals(htmlExpected, element.html());
    }

}

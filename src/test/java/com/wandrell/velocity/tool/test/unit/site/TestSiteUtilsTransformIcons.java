/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015-2017 the original author or authors.
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

package com.wandrell.velocity.tool.test.unit.site;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.velocity.tool.SiteUtils;

/**
 * Unit tests for {@link SiteUtils}, testing the {@code transformIcons} method.
 * 
 * @author Bernardo Martínez Garrido
 * @see SiteUtils
 */
public final class TestSiteUtilsTransformIcons {

    /**
     * Instance of the utils class being tested.
     */
    private final SiteUtils util = new SiteUtils();

    /**
     * Default constructor.
     */
    public TestSiteUtilsTransformIcons() {
        super();
    }

    /**
     * Tests that when finding an expected icon it is transformed correctly.
     */
    @Test
    public final void testIcon_Transforms() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "<img src=\"images/add.gif\" alt=\"An image\">";

        element = Jsoup.parse(html).body();
        util.transformIcons(element);

        htmlExpected = "<span><span class=\"fa fa-plus\" aria-hidden=\"true\"></span><span class=\"sr-only\">Addition</span></span>";

        Assert.assertEquals(element.html(), htmlExpected);
    }

    /**
     * Tests that HTML with no icons is left untouched
     */
    @Test
    public final void testNoIcons_Untouched() {
        final String html;         // HTML code to edit
        final String htmlExpected; // Expected result
        final Element element;     // Parsed HTML

        html = "<p>Some text</p>";

        element = Jsoup.parse(html).body();
        util.transformIcons(element);

        htmlExpected = html;

        Assert.assertEquals(element.html(), htmlExpected);
    }

}

/*
 * Copyright 2015 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmax.elementspec;

import org.openqa.selenium.By;

/**
 * {@code ElementSpecification} is the entirety of the public API for creating selectors. It provides three categories of methods:
 *
 * <h3>Static Starting Points</h3>
 * <dl>
 * <dt><code>anElement()</code></dt>
 * <dd>Literally any element on the page.</dd>
 *
 * <dt><code>anElementOfType(tagName)</code></dt>
 * <dd>Any element of the specified type, e.g. table.</dd>
 *
 * <dt><code>anElementWithClass(classname)</code></dt>
 * <dd>Select an element with a given classname.  Equivalent to the css `.classname`.</dd>
 *
 * <dt><code>anElementWithId(id)</code></dt>
 * <dd>Select an element by ID. Equivalent to the css `#id`.</dd>
 * </dl>
 *
 * <p>Using static imports to access these methods is strongly recommended to aid readability.</p>
 *
 * <h3>{@code With} Refining Methods</h3>
 * These methods add additional requirements to the current element. For example {@link #withId(String)}, {@link #withClass(String)} and {@link #withAttribute(String)}.
 *
 * <h3>{@code thatContains} Descender Methods</h3>
 * These methods search within the current element and descend the DOM tree to the matching element. Any future {@code with} methods will operate on this new element.
 * For example {@link #thatContainsAnyElement()}, {@link #thatContainsA(String)} and {@link #thatContainsAnElementWithClass(String)}.
 *
 * <h3>Immutability</h3>
 * ElementSpecification instances are immutable and thread safe - each method call returns a new ElementSpecification instance making it safe to store instances in static variables
 * to avoid duplication.
 */
public interface ElementSpecification
{
    /**
     * Select any element with the tag {@code tagName}.
     *
     * @param tagName the targeted element name, e.g. {@code table} or {@code button}.
     * @return the new {@code ElementSpecification}.
     */
    static ElementSpecification anElementOfType(final String tagName)
    {
        return new MultiFormatElementSpecification(
                CssElementSpecification.anElementOfType(tagName),
                XPathElementSpecification.anElementOfType(tagName)
        );
    }

    /**
     * Select any element with no restrictions. Restrictions can be added by calling the refining instance methods.
     *
     * @return the new {@code ElementSpecification}
     */
    static ElementSpecification anElement()
    {
        return new MultiFormatElementSpecification(
                CssElementSpecification.anElement(),
                XPathElementSpecification.anElement()
        );
    }

    /**
     * Select an element by ID.
     *
     * @param id the ID of the element to select.
     * @return the new {@code ElementSpecification}.
     */
    static ElementSpecification anElementWithId(final String id)
    {
        return new MultiFormatElementSpecification(
                IdElementSpecification.anElementWithId(id),
                CssElementSpecification.anElement().withId(id),
                XPathElementSpecification.anElement().withId(id)
        );
    }

    /**
     * Select an element by class name.
     *
     * @param classname the class name of the element to select.
     * @return the new {@code ElementSpecification}.
     */
    static ElementSpecification anElementWithClass(final String classname)
    {
        return anElement().withClass(classname);
    }

    /**
     * Create an {@code ElementSpecification} from a selenium 1 style selector. i.e. CSS selectors are prefixed with {@code css=}, xpath selectors start with either {@code xpath=} or {@code //}.
     *
     * @param oldStyleSeleniumLocator the selenium 1 selector to create an ElementSpecification from.
     * @return the new {@code ElementSpecification}.
     */
    static ElementSpecification fromOldStyleSeleniumLocator(final String oldStyleSeleniumLocator)
    {
        if (oldStyleSeleniumLocator.startsWith("css="))
        {
            return CssElementSpecification.fromOldStyleSeleniumCssLocator(oldStyleSeleniumLocator.substring("css=".length()));
        }
        else if (oldStyleSeleniumLocator.startsWith("xpath="))
        {
            return XPathElementSpecification.fromOldStyleSeleniumXPathLocator(oldStyleSeleniumLocator.substring("xpath=".length()));
        }
        else if (oldStyleSeleniumLocator.startsWith("//") || oldStyleSeleniumLocator.startsWith("(//"))
        {
            return XPathElementSpecification.fromOldStyleSeleniumXPathLocator(oldStyleSeleniumLocator);
        }
        else
        {
            return ElementSpecification.anElementWithId(oldStyleSeleniumLocator);
        }
    }

    /**
     * Utility for converting a selenium 1 style locator straight to a WebDriver style selector.
     *
     * @param oldStyleSeleniumLocator the selenium 1 selector
     * @return an equivalent WebDriver {@code By} selector.
     */
    static By by(final String oldStyleSeleniumLocator)
    {
        return fromOldStyleSeleniumLocator(oldStyleSeleniumLocator).asWebDriverLocator();
    }

    /**
     * Select a descendant of the current element with the element name {@code tagName}.
     *
     * @param tagName the targeted element name.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification thatContainsA(String tagName);

    /**
     * Select a child element of the specified type. Unlike {@link #thatContainsA(String)} this method only considers direct child elements, not any descendant.
     *
     * @param tagName the targeted element name.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification thatContainsAChildOfType(String tagName);

    /**
     * Select a descendant of the current element with the ID {@code id}.
     *
     * @param id the ID of the element to select.
     * @return the new {@code ElementSpecification}.
     */
    default ElementSpecification thatContainsAnElementWithId(final String id)
    {
        return thatContainsAnyElement().withId(id);
    }

    /**
     * Select a descendant of the current element with the class {@code classname}.
     *
     * @param classname the targeted class name.
     * @return the new {@code ElementSpecification}.
     */
    default ElementSpecification thatContainsAnElementWithClass(final String classname)
    {
        return thatContainsAnyElement().withClass(classname);
    }

    /**
     * Append another {@code ElementSpecification} to this one.
     *
     * @param builder the specification to append.
     * @return the new, combined {@code ElementSpecification}.
     */
    ElementSpecification addSubSpecification(ElementSpecification builder);

    /**
     * Select a descendant of the current element. Restrictions can be added using chained {@code with} methods.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification thatContainsAnyElement();

    /**
     * Require the current element to have the id {@code id}.
     *
     * @param id the targeted ID.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withId(String id);

    /**
     * Require the current element to have the class {@code classname}.
     *
     * @param classname the targeted class.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withClass(String classname);

    /**
     * Require the current element to have any of the specified classes.
     *
     * @param classnames the acceptable class names
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withAnyOfTheseClasses(String... classnames);

    /**
     * Require the current element to <i>not</i> have the specified class.
     *
     * @param classname the unwanted classname.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withoutClass(String classname);

    /**
     * Require the current element to have an attribute {@code attributeName} with any value.
     *
     * @param attributeName the name of the attribute.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withAttribute(String attributeName);

    /**
     * Require the current element to <i>not</i> have an attribute {@code attributeName}.
     *
     * @param attributeName the name of the unwanted attribute.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withoutAttribute(String attributeName);

    /**
     * Require the current element to have an attribute {@code attributeName} with a value that contains {@code expectedSubstring}.
     *
     * @param attributeName the name of the attribute.
     * @param expectedSubstring the expected substring.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withAttributeContaining(String attributeName, String expectedSubstring);

    /**
     * Require the current element to have an attribute {@code attributeName} with a value that is exactly equal to {@code value}.
     *
     * @param attributeName the attribute name.
     * @param value the expected value.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withAttributeValue(String attributeName, String value);

    /**
     * Require the current element to be in position {@code position}. Element positions are numbered starting from 1.
     *
     * <p>So given the DOM structure:</p>
     *
     * <pre>{@code
     * <div>
     *     <p>Line 1</p>
     *     <p>Line 2</p>
     *     <p>Line 3</p>
     * </div>
     * }</pre>
     *
     * <p>The selector: {@code anElement().inPosition(2)} would select the middle {@code p} with the text "Line 2".</p>
     *
     * @param position the required position.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification inPosition(int position);

    /**
     * Require the current element to be in position {@code position} with respect to the elements of the same type.
     * This is best used after an entry that specifies a tag name.
     *
     * <p>So given the DOM structure:</p>
     *
     * <pre>{@code
     * <div>
     *     <span/>
     *     <p>Line 1</p>
     *     <p>Line 2</p>
     *     <p>Line 3</p>
     * </div>
     * }</pre>
     *
     * <p>The selector: {@code ...thatContainsA("p").inPosition(2)} would select the
     * middle {@code p} with the text "Line 2".</p>
     *
     * @param position the required position.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification inPositionOfType(int position);

    /**
     * Require the text content of the current element to be {@code text}.
     *
     * @param text the expected text.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withText(String text);

    /**
     * Require the text content of the current element to contain {@code text} as either a complete or a substring match.
     *
     * @param text the expected substring.
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withTextContaining(String text);

    /**
     * Require the current element to contain numerical content. Numerical content is anything that the XPath {@code number()} function can parse.
     *
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withNumericalContent();

    /**
     * Require the current element to be empty.
     *
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification withNoChildren();

    /**
     * Require the current element to be checked. Generally only applicable to {@code checkbox} input elements.
     *
     * @return the new {@code ElementSpecification}.
     */
    ElementSpecification thatIsChecked();

    /**
     * Determine if this specification can be represented in any of the supported formats.
     *
     * @return {@code true} if an only if this specification can be converted into a valid locator using either {@link #asSeleniumLocator()} or {@link #asWebDriverLocator()}.
     */
    boolean isValid();

    /**
     * Convert this specification into a selenium 1 locator.
     *
     * @return a selenium 1 compatible locator. The format will be identified correctly (i.e. CSS locators will be prefixed with {@code css=}.
     */
    String asSeleniumLocator();

    /**
     * Convert this specification into a WebDriver {@link By} locator.
     *
     * @return the {@code By} locator equivalent to this {@code ElementSpecification}.
     */
    By asWebDriverLocator();
}

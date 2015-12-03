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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


import static com.lmax.elementspec.XPathElementSpecification.anElement;
import static com.lmax.elementspec.XPathElementSpecification.anElementOfType;

public class XPathElementSpecificationTest
{


    @Test
    public void shouldBeAbleToFindAnyElement() throws Exception
    {
        assertXPath(anElement(), "//*");
    }

    @Test
    public void shouldBeAbleToFindElementWithTagName() throws Exception
    {
        assertXPath(anElementOfType("input"), "//input");
    }

    @Test
    public void shouldAllowFindingElementWithClassButWithoutAttribute() throws Exception
    {
        assertXPath(anElementOfType("span").withClass("foo").withoutAttribute("title"), "//span[contains(concat(' ', @class, ' '), ' foo ')][not(@title)]");
    }

    @Test
    public void shouldCorrectlyIdentifyElementsWithAnAttributePresent() throws Exception
    {
        assertXPath(anElementOfType("input").withAttribute("disabled"), "//input[@disabled]");
    }

    @Test
    public void shouldCorrectlyIdentifyElementsWithNoChildren() throws Exception
    {
        assertXPath(anElementOfType("div").withNoChildren(), "//div[not(node())]");
    }

    @Test
    public void shouldAllowFindingElementWithoutClass() throws Exception
    {
        assertXPath(anElementOfType("span").withoutClass("a"), "//span[not(contains(concat(' ', @class, ' '), ' a '))]");
    }

    @Test
    public void shouldAllowFindingElementWithOneOfTheseClasses() throws Exception
    {
        assertXPath(anElementOfType("span").withAnyOfTheseClasses("a", "b", "c"),
                    "//span[contains(concat(' ', @class, ' '), ' a ') or contains(concat(' ', @class, ' '), ' b ') or contains(concat(' ', @class, ' '), ' c ')]");
    }







    @Test
    public void shouldCreateXPathSelectorForAnElementOfType() throws Exception
    {
        assertXPath(anElementOfType("div"), "//div");
    }

    @Test
    public void shouldCreateXPathForAnyElement() throws Exception
    {
        assertXPath(anElement(), "//*");
    }

    @Test
    public void shouldCreateXPathForElementWithId() throws Exception
    {
        assertXPath(anElement().withId("foo"), "//*[@id='foo']");
    }

    @Test
    public void shouldCreateXPathForNestedElements() throws Exception
    {
        assertXPath(anElementOfType("p").thatContainsA("input"), "//p//input");
    }

    @Test
    public void shouldCreateXPathForElementWithIdThatContainsAnElement() throws Exception
    {
        assertXPath(anElement().withId("foo").thatContainsA("a"), "//*[@id='foo']//a");
    }

    @Test
    public void shouldCreateXPathForElementWithIdThatContainsAnyElementThatContainsElementWithId() throws Exception
    {
        assertXPath(anElement().withId("one").thatContainsAnyElement().thatContainsAnyElement().withId("two"), "//*[@id='one']//*//*[@id='two']");
    }

    @Test
    public void shouldCreateXPathForElementWithAttribute() throws Exception
    {
        assertXPath(anElement().withAttribute("myAttr"), "//*[@myAttr]");
    }

    @Test
    public void shouldCreateXPathForElementWithIdAndAttribute() throws Exception
    {
        assertXPath(anElement().withId("foo").withAttribute("bar"), "//*[@id='foo'][@bar]");
    }

    @Test
    public void shouldCreateXPathForElementOfTypeWithAttribute() throws Exception
    {
        assertXPath(anElementOfType("a").withAttribute("bar"), "//a[@bar]");
    }

    @Test
    public void shouldCreateXPathForElementOfTypeWithAttributeAndID() throws Exception
    {
        assertXPath(anElementOfType("div").withId("foo").withAttribute("myattr"), "//div[@id='foo'][@myattr]");
    }

    @Test
    public void shouldCreateXPathForElementWithAttributeSpecifiedBeforeID() throws Exception
    {
        assertXPath(anElement().withAttribute("dog").withId("cat"), "//*[@dog][@id='cat']");
    }

    @Test
    public void shouldCreateXPathForElementWithAttributeValue() throws Exception
    {
        assertXPath(anElement().withAttributeValue("foo", "bar"), "//*[@foo='bar']");
    }

    @Test
    public void shouldCreateXPathForElementWithIdAndAttributeValue() throws Exception
    {
        assertXPath(anElement().withId("foo").withAttributeValue("attr", "value"), "//*[@id='foo'][@attr='value']");
    }

    @Test
    public void shouldCreateXPathForElementWithAttributeAndAttributeWithSpecificValue() throws Exception
    {
        assertXPath(anElementOfType("div").withAttribute("attr1").withAttributeValue("attr2", "value2"), "//div[@attr1][@attr2='value2']");
    }

    @Test
    public void shouldCreateXPathForElementWithAttributeContainingValue() throws Exception
    {
        assertXPath(anElement().withAttributeContaining("attr", "substring"), "//*[contains(@attr, 'substring')]");
    }

    @Test
    public void shouldCreateXPathForElementWithIdAndAttributeAndSpecificAttributeValueAndAttributeContainingValueThatContainsAnyElement() throws Exception
    {
        assertXPath(anElementOfType("p").withAttributeContaining("attr1", "val1").withAttributeValue("attr2", "val2").withAttribute("attr3").withId(
                            "foo").thatContainsAnyElement(),
                    "//p[contains(@attr1, 'val1')][@attr2='val2'][@attr3][@id='foo']//*");
    }

    @Test
    public void shouldCreateXPathForElementWithAChild() throws Exception
    {
        assertXPath(anElement().thatContainsAChildOfType("p"), "//*/p");
    }

    @Test
    public void shouldCreateXPathForEmptyElement() throws Exception
    {
        assertXPath(anElement().withNoChildren(), "//*[not(node())]");
    }

    @Test
    public void shouldCreateXPathForElementWithIdWithAChild() throws Exception
    {
        assertXPath(anElement().withId("foo").thatContainsAChildOfType("p"), "//*[@id='foo']/p");
    }

    @Test
    public void shouldCreateXPathForElementWIthIdAndAttributeWithAChildWithIdAndAttribute() throws Exception
    {
        assertXPath(anElementOfType("div").withAttribute("attr1").withId("parent").thatContainsAChildOfType("p").withId("child").withAttribute("attr2"),
                    "//div[@attr1][@id='parent']/p[@id='child'][@attr2]");
    }

    @Test
    public void shouldCreateXPathForElementWithClass() throws Exception
    {
        assertXPath(anElement().withClass("bar"), "//*[contains(concat(' ', @class, ' '), ' bar ')]");
    }

    @Test
    public void shouldCreateXPathForElementOfTypeWithClass() throws Exception
    {
        assertXPath(anElementOfType("div").withClass("bar"), "//div[contains(concat(' ', @class, ' '), ' bar ')]");
    }

    @Test
    public void shouldCreateElementOfTypeWithIdAttributeAndClassThatHasAChild() throws Exception
    {
        assertXPath(anElementOfType("div").withAttribute("attr").withClass("bar").withId("foo").thatContainsAChildOfType("p"),
                    "//div[@attr][contains(concat(' ', @class, ' '), ' bar ')][@id='foo']/p");
    }

    @Test
    public void shouldCreateElementOfTypeThatContainsAnyElementWithAClass() throws Exception
    {
        assertXPath(anElementOfType("div").thatContainsAnyElement().withClass("bar"), "//div//*[contains(concat(' ', @class, ' '), ' bar ')]");
    }

    @Test
    public void shouldCreateXPathForElementInPosition() throws Exception
    {
        assertXPath(anElement().inPosition(3), "//*[3]");
    }

    @Test
    public void shouldCreateXPathForElementInPositionWithIdAndAttributeShouldOrderSelectorsCorrectly() throws Exception
    {
        assertXPath(anElement().withAttribute("attr").inPosition(3).withId("foo"), "//*[@attr][3][@id='foo']");
    }

    @Test
    public void shouldCreateXPathSelectorForWithText() throws Exception
    {
        assertXPath(anElement().withText("foo"), "//*[text() = 'foo']");
    }

    @Test
    public void shouldCreateXPathSelectorForNumericalContent() throws Exception
    {
        assertXPath(anElement().withNumericalContent(), "//*[number(.)=.]");
    }

    @Test
    public void shouldCreateXPathForElementWithClass1ButNotClass2() throws Exception
    {
        assertXPath(anElement().withClass("one").withoutClass("two"), "//*[contains(concat(' ', @class, ' '), ' one ')][not(contains(concat(' ', @class, ' '), ' two '))]");
    }

    @Test
    public void shouldCreateXPathForElementWithInvalidId() throws Exception
    {
        assertXPath(anElement().withId("EUR/USD"), "//*[@id='EUR/USD']");
    }

    @Test
    public void shouldCreateXPathForCheckedCheckbox() throws Exception
    {
        assertInvalid(anElement().withAttributeValue("type", "checkbox").thatIsChecked());
    }


    private void assertInvalid(final ElementSpecification builder)
    {
        assertThat(builder.isValid(), is(false));
    }


    private void assertXPath(final ElementSpecification builder, final String expected)
    {
        assertThat(builder.asSeleniumLocator(), is(expected));
    }
}

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import static com.lmax.elementspec.CssElementSpecification.anElement;
import static com.lmax.elementspec.CssElementSpecification.anElementOfType;

public class CssElementSpecificationTest
{
    @Test
    public void shouldCreateCssSelectorForAnElementOfType() throws Exception
    {
        assertCss(anElementOfType("div"), "div");
    }

    @Test
    public void shouldCreateCssForAnyElement() throws Exception
    {
        assertCss(anElement(), "*");
    }

    @Test
    public void shouldCreateCssForElementWithId() throws Exception
    {
        assertCss(anElement().withId("foo"), "#foo");
    }

    @Test
    public void shouldCreateCssForNestedElements() throws Exception
    {
        assertCss(anElementOfType("p").thatContainsA("input"), "p input");
    }

    @Test
    public void shouldCreateCssForElementWithIdThatContainsAnElement() throws Exception
    {
        assertCss(anElement().withId("foo").thatContainsA("a"), "#foo a");
    }

    @Test
    public void shouldCreateCssForElementWithIdThatContainsAnyElementThatContainsElementWithId() throws Exception
    {
        assertCss(anElement().withId("one").thatContainsAnyElement().thatContainsAnyElement().withId("two"), "#one * #two");
    }

    @Test
    public void shouldCreateCssForElementWithAttribute() throws Exception
    {
        assertCss(anElement().withAttribute("myAttr"), "*[myAttr]");
    }

    @Test
    public void shouldCreateCssForElementWithIdAndAttribute() throws Exception
    {
        assertCss(anElement().withId("foo").withAttribute("bar"), "#foo[bar]");
    }

    @Test
    public void shouldCreateCssForElementOfTypeWithAttribute() throws Exception
    {
        assertCss(anElementOfType("a").withAttribute("bar"), "a[bar]");
    }

    @Test
    public void shouldCreateCssForElementOfTypeWithAttributeAndID() throws Exception
    {
        assertCss(anElementOfType("div").withId("foo").withAttribute("myattr"), "div#foo[myattr]");
    }

    @Test
    public void shouldCreateCssForElementWithAttributeSpecifiedBeforeID() throws Exception
    {
        assertCss(anElement().withAttribute("dog").withId("cat"), "#cat[dog]");
    }

    @Test
    public void shouldCreateCssForElementWithAttributeValue() throws Exception
    {
        assertCss(anElement().withAttributeValue("foo", "bar"), "*[foo=\"bar\"]");
    }

    @Test
    public void shouldCreateCssForElementWithIdAndAttributeValue() throws Exception
    {
        assertCss(anElement().withId("foo").withAttributeValue("attr", "value"), "#foo[attr=\"value\"]");
    }

    @Test
    public void shouldCreateCssForElementWithAttributeAndAttributeWithSpecificValue() throws Exception
    {
        assertCss(anElementOfType("div").withAttribute("attr1").withAttributeValue("attr2", "value2"), "div[attr1][attr2=\"value2\"]");
    }

    @Test
    public void shouldCreateCssForElementWithAttributeContainingValue() throws Exception
    {
        assertCss(anElement().withAttributeContaining("attr", "substring"), "*[attr*=\"substring\"]");
    }

    @Test
    public void shouldCreateCssForElementWithIdAndAttributeAndSpecificAttributeValueAndAttributeContainingValueThatContainsAnyElement() throws Exception
    {
        assertCss(anElementOfType("p").withAttributeContaining("attr1", "val1").withAttributeValue("attr2", "val2").withAttribute("attr3").withId("foo").thatContainsAnyElement(),
                  "p#foo[attr1*=\"val1\"][attr2=\"val2\"][attr3] *");
    }

    @Test
    public void shouldCreateCssForElementWithAChild() throws Exception
    {
        assertCss(anElement().thatContainsAChildOfType("p"), "* > p");
    }

    @Test
    public void shouldCreateCssForEmptyElement() throws Exception
    {
        assertCss(anElement().withNoChildren(), ":empty");
    }

    @Test
    public void shouldCreateCssForElementWithIdWithAChild() throws Exception
    {
        assertCss(anElement().withId("foo").thatContainsAChildOfType("p"), "#foo > p");
    }

    @Test
    public void shouldCreateCssForElementWIthIdAndAttributeWithAChildWithIdAndAttribute() throws Exception
    {
        assertCss(anElementOfType("div").withAttribute("attr1").withId("parent").thatContainsAChildOfType("p").withId("child").withAttribute("attr2"), "div#parent[attr1] > p#child[attr2]");
    }

    @Test
    public void shouldCreateCssForElementWithClass() throws Exception
    {
        assertCss(anElement().withClass("bar"), ".bar");
    }

    @Test
    public void shouldCreateCssForElementOfTypeWithClass() throws Exception
    {
        assertCss(anElementOfType("div").withClass("bar"), "div.bar");
    }

    @Test
    public void shouldCreateElementOfTypeWithIdAttributeAndClassThatHasAChild() throws Exception
    {
        assertCss(anElementOfType("div").withAttribute("attr").withClass("bar").withId("foo").thatContainsAChildOfType("p"), "div#foo.bar[attr] > p");
    }

    @Test
    public void shouldCreateElementOfTypeThatContainsAnyElementWithAClass() throws Exception
    {
        assertCss(anElementOfType("div").thatContainsAnyElement().withClass("bar"), "div .bar");
    }

    @Test
    public void shouldCreateCssForElementInPosition() throws Exception
    {
        assertCss(anElement().inPosition(3), ":nth-child(3)");
    }

    @Test
    public void shouldCreateCssForElementInPositionWithIdAndAttributeShouldOrderSelectorsCorrectly() throws Exception
    {
        assertCss(anElement().withAttribute("attr").inPosition(3).withId("foo"), "#foo:nth-child(3)[attr]");
    }

    @Test
    public void shouldCreateCssForElementInPositionOfType() throws Exception
    {
        assertCss(anElement().inPositionOfType(3), ":nth-of-type(3)");
    }

    @Test
    public void shouldCreateCssForElementInPositionOfTypeWithTagName() throws Exception
    {
        assertCss(anElementOfType("input").inPositionOfType(3), "input:nth-of-type(3)");
    }

    @Test
    public void shouldCreateCssForElementInPositionOfTypeWithIdAndAttributeShouldOrderSelectorsCorrectly() throws Exception
    {
        assertCss(anElement().withAttribute("attr").inPositionOfType(3).withId("foo"), "#foo:nth-of-type(3)[attr]");
    }

    @Test
    public void shouldBeInvalidIfWithTextIsUsed() throws Exception
    {
        assertInvalid(anElement().withText("foo"));
    }

    @Test
    public void shouldBeInvalidIfWithNumericalContentIsUsed() throws Exception
    {
        assertInvalid(anElement().withNumericalContent());
    }

    @Test
    public void shouldCreateCssForElementWithClass1ButNotClass2() throws Exception
    {
        assertCss(anElement().withClass("one").withoutClass("two"), ".one:not(.two)");
    }

    @Test
    public void shouldCreateCssForElementWithInvalidId() throws Exception
    {
        assertCss(anElement().withId("EUR/USD"), "*[id=\"EUR/USD\"]");
    }

    @Test
    public void shouldCreateCssForCheckedCheckbox() throws Exception
    {
        assertCss(anElement().withAttributeValue("type", "checkbox").thatIsChecked(), ":checked[type=\"checkbox\"]");
    }

    @Test
    public void shouldAllowFindingElementWithClassButWithoutAttribute() throws Exception
    {
        assertCss(anElementOfType("span").withClass("foo").withoutAttribute("title"), "span.foo:not([title])");
    }

    private void assertCss(final ElementSpecification builder, final String expectedCss)
    {
        assertTrue("Should have been valid", builder.isValid());
        assertThat(builder.asSeleniumLocator(), is("css=" + expectedCss));
    }

    private void assertInvalid(final ElementSpecification builder)
    {
        assertFalse("Should have been invalid", builder.isValid());
        try
        {
            builder.asSeleniumLocator();
            fail("Should have thrown IllegalStateException");
        }
        catch (final IllegalStateException e)
        {
            // Expected
        }
    }
}

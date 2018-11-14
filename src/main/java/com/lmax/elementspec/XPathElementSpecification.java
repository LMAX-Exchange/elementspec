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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lmax.elementspec.InvalidElementSpecification.INVALID;

final class XPathElementSpecification implements ElementSpecification
{
    private final String xpath;

    private XPathElementSpecification(final String xpathSoFar)
    {
        xpath = xpathSoFar;
    }

    static XPathElementSpecification fromOldStyleSeleniumXPathLocator(final String oldStyleSeleniumXPathLocator)
    {
        return new XPathElementSpecification(oldStyleSeleniumXPathLocator);
    }

    public static XPathElementSpecification anElementOfType(final String tagName)
    {
        return new XPathElementSpecification("//" + tagName);
    }

    public static XPathElementSpecification anElement()
    {
        return new XPathElementSpecification("//*");
    }

    @Override
    public ElementSpecification withId(final String id)
    {
        return appendCondition("@id='%s'", id);
    }

    @Override
    public ElementSpecification thatContainsA(final String tagName)
    {
        return appendAncestorSelector(tagName);
    }

    @Override
    public ElementSpecification addSubSpecification(final ElementSpecification builder)
    {
        if (builder instanceof XPathElementSpecification && builder.isValid())
        {
            return append(((XPathElementSpecification)builder).getCurrentXPath());
        }
        return INVALID;
    }

    @Override
    public ElementSpecification thatContainsAnyElement()
    {
        return appendAncestorSelector("*");
    }

    @Override
    public ElementSpecification withAttribute(final String attributeName)
    {
        return appendCondition("@" + attributeName);
    }

    @Override
    public ElementSpecification withoutAttribute(final String attributeName)
    {
        return appendCondition("not(@" + attributeName + ")");
    }

    @Override
    public ElementSpecification thatContainsAChildOfType(final String tagName)
    {
        return append("/" + tagName);
    }

    @Override
    public ElementSpecification withClass(final String classname)
    {
        return appendCondition(hasClassCondition(classname));
    }

    @Override
    public ElementSpecification withAnyOfTheseClasses(final String... classnames)
    {
        return appendCondition(Stream.of(classnames)
                                       .map(this::hasClassCondition)
                                       .collect(Collectors.joining(" or ")));
    }

    @Override
    public ElementSpecification withoutClass(final String classname)
    {
        return appendCondition("not(" + hasClassCondition(classname) + ")");
    }

    @Override
    public ElementSpecification inPosition(final int position)
    {
        return appendCondition(Integer.toString(position));
    }

    @Override
    public ElementSpecification inPositionOfType(int position)
    {
        return appendCondition(Integer.toString(position));
    }

    @Override
    public ElementSpecification withText(final String text)
    {
        if (text.isEmpty())
        {
            return withNoChildren();
        }
        else
        {
            return appendCondition("text() = '" + text + "'");
        }
    }

    @Override
    public ElementSpecification withTextContaining(final String text)
    {
        return appendCondition("text()[contains(.,'" + text + "')]");
    }

    @Override
    public ElementSpecification withAttributeContaining(final String attributeName, final String expectedSubstring)
    {
        return appendCondition("contains(@" + attributeName + ", '" + expectedSubstring + "')");
    }

    @Override
    public ElementSpecification withAttributeValue(final String attributeName, final String value)
    {
        return appendCondition("@" + attributeName + "='%s'", value);
    }

    @Override
    public ElementSpecification withNumericalContent()
    {
        // Because if it's not a number number(.) will be NaN and that isn't equal to anything. If it is a number then the plain . will be cast to a number and the comparison succeeds.
        return appendCondition("number(.)=.");
    }

    @Override
    public ElementSpecification withNoChildren()
    {
        return appendCondition("not(node())");
    }

    @Override
    public ElementSpecification thatIsChecked()
    {
        return INVALID;
    }

    public String getCurrentXPath()
    {
        return xpath;
    }

    @Override
    public String asSeleniumLocator()
    {
        return getCurrentXPath();
    }

    @Override
    public By asWebDriverLocator()
    {
        return By.xpath(getCurrentXPath());
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return asSeleniumLocator();
    }

    private String hasClassCondition(final String classname)
    {
        return "contains(concat(' ', @class, ' '), ' " + classname + " ')";
    }

    private ElementSpecification appendCondition(final String condition, final Object... args)
    {
        if (args.length == 0)
        {
            return append("[" + condition + "]");
        }
        else
        {
            return append("[" + String.format(condition, args) + "]");
        }
    }

    private ElementSpecification appendAncestorSelector(final String tagName)
    {
        return append("//" + tagName);
    }

    private ElementSpecification append(final String section)
    {
        return new XPathElementSpecification(xpath + section);
    }
}

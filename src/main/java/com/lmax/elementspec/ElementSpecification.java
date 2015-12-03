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

public interface ElementSpecification
{
    static ElementSpecification anElementOfType(final String tagName)
    {
        return new MultiFormatElementSpecification(
                CssElementSpecification.anElementOfType(tagName),
                XPathElementSpecification.anElementOfType(tagName)
        );
    }

    static ElementSpecification anElement()
    {
        return new MultiFormatElementSpecification(
                CssElementSpecification.anElement(),
                XPathElementSpecification.anElement()
        );
    }

    static ElementSpecification anElementWithId(final String id)
    {
        return new MultiFormatElementSpecification(
                IdElementSpecification.anElementWithId(id),
                CssElementSpecification.anElement().withId(id),
                XPathElementSpecification.anElement().withId(id)
        );
    }

    static ElementSpecification anElementWithClass(final String classname)
    {
        return anElement().withClass(classname);
    }

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

    static By by(final String oldStyleSeleniumLocator)
    {
        return fromOldStyleSeleniumLocator(oldStyleSeleniumLocator).asWebDriverLocator();
    }

    ElementSpecification thatContainsA(String tagName);

    default ElementSpecification thatContainsAnElementWithId(final String id)
    {
        return thatContainsAnyElement().withId(id);
    }

    default ElementSpecification thatContainsAnElementWithClass(final String classname)
    {
        return thatContainsAnyElement().withClass(classname);
    }

    ElementSpecification addSubSpecification(ElementSpecification builder);

    ElementSpecification thatContainsAnyElement();

    ElementSpecification withId(String id);

    ElementSpecification withClass(String classname);

    ElementSpecification withAnyOfTheseClasses(String... classnames);

    ElementSpecification withoutClass(String classname);

    ElementSpecification withAttribute(String attributeName);

    ElementSpecification withoutAttribute(String attributeName);

    ElementSpecification thatContainsAChildOfType(String tagName);

    ElementSpecification inPosition(int position);

    ElementSpecification withText(String text);

    ElementSpecification withTextContaining(String text);

    ElementSpecification withAttributeContaining(String attributeName, String expectedSubstring);

    ElementSpecification withAttributeValue(String attributeName, String value);

    ElementSpecification withNumericalContent();

    ElementSpecification withNoChildren();

    ElementSpecification thatIsChecked();

    boolean isValid();

    String asSeleniumLocator();

    By asWebDriverLocator();
}

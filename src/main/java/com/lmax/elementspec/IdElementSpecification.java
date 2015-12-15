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


import static com.lmax.elementspec.InvalidElementSpecification.INVALID;

final class IdElementSpecification implements ElementSpecification
{
    private final String id;

    private IdElementSpecification(final String id)
    {
        this.id = id;
    }

    public static IdElementSpecification anElementWithId(final String id)
    {
        return new IdElementSpecification(id);
    }

    @Override
    public ElementSpecification withId(final String id)
    {
        return anElementWithId(id);
    }

    @Override
    public ElementSpecification thatContainsA(final String tagName)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification addSubSpecification(final ElementSpecification builder)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification thatContainsAnyElement()
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withAttribute(final String attributeName)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withoutAttribute(final String attributeName)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification thatContainsAChildOfType(final String tagName)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withClass(final String classname)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withAnyOfTheseClasses(final String... classnames)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withoutClass(final String classname)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification inPosition(final int position)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withText(final String text)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withTextContaining(final String text)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withAttributeContaining(final String attributeName, final String expectedSubstring)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withAttributeValue(final String attributeName, final String value)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withNumericalContent()
    {
        return INVALID;
    }

    @Override
    public ElementSpecification withNoChildren()
    {
        return INVALID;
    }

    @Override
    public ElementSpecification thatIsChecked()
    {
        return INVALID;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public String asSeleniumLocator()
    {
        return id;
    }

    @Override
    public By asWebDriverLocator()
    {
        return By.id(id);
    }

    @Override
    public String toString()
    {
        return asSeleniumLocator();
    }
}

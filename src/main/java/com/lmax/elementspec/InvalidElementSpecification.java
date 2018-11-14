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

class InvalidElementSpecification implements ElementSpecification
{
    public static final InvalidElementSpecification INVALID = new InvalidElementSpecification();

    @Override
    public ElementSpecification withId(final String id)
    {
        return this;
    }

    @Override
    public ElementSpecification thatContainsA(final String tagName)
    {
        return this;
    }

    @Override
    public ElementSpecification addSubSpecification(final ElementSpecification builder)
    {
        return this;
    }

    @Override
    public ElementSpecification thatContainsAnyElement()
    {
        return this;
    }

    @Override
    public ElementSpecification withAttribute(final String attributeName)
    {
        return this;
    }

    @Override
    public ElementSpecification withoutAttribute(final String attributeName)
    {
        return this;
    }

    @Override
    public ElementSpecification thatContainsAChildOfType(final String tagName)
    {
        return this;
    }

    @Override
    public ElementSpecification withClass(final String classname)
    {
        return this;
    }

    @Override
    public ElementSpecification withAnyOfTheseClasses(final String... classnames)
    {
        return this;
    }

    @Override
    public ElementSpecification inPosition(final int position)
    {
        return this;
    }

    @Override
    public ElementSpecification inPositionOfType(int position)
    {
        return this;
    }

    @Override
    public ElementSpecification withText(final String text)
    {
        return this;
    }

    @Override
    public ElementSpecification withTextContaining(final String text)
    {
        return this;
    }

    @Override
    public ElementSpecification withAttributeContaining(final String attributeName, final String expectedSubstring)
    {
        return this;
    }

    @Override
    public ElementSpecification withAttributeValue(final String attributeName, final String value)
    {
        return this;
    }

    @Override
    public ElementSpecification withNumericalContent()
    {
        return this;
    }

    @Override
    public ElementSpecification withNoChildren()
    {
        return this;
    }

    @Override
    public ElementSpecification thatIsChecked()
    {
        return this;
    }

    @Override
    public ElementSpecification withoutClass(final String classname)
    {
        return this;
    }

    @Override
    public boolean isValid()
    {
        return false;
    }

    @Override
    public String asSeleniumLocator()
    {
        throw new IllegalStateException("Unable to build a valid element specification.");
    }

    @Override
    public By asWebDriverLocator()
    {
        throw new IllegalStateException("Unable to build a valid element specification.");
    }

    @Override
    public String toString()
    {
        return "<INVALID SELECTOR>";
    }
}

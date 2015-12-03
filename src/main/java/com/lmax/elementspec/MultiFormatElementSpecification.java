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

import java.util.Collection;
import java.util.function.UnaryOperator;

import static com.lmax.elementspec.InvalidElementSpecification.INVALID;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

final class MultiFormatElementSpecification implements ElementSpecification
{
    private final Collection<ElementSpecification> builders;

    MultiFormatElementSpecification(final ElementSpecification... builders)
    {
        this.builders = asList(builders);
    }

    private MultiFormatElementSpecification(final Collection<ElementSpecification> builders)
    {
        this.builders = builders;
    }

    @Override
    public ElementSpecification withId(final String id)
    {
        return each(builder -> builder.withId(id));
    }

    @Override
    public ElementSpecification thatContainsA(final String tagName)
    {
        return each(builder -> builder.thatContainsA(tagName));
    }

    @Override
    @SuppressWarnings("Convert2MethodRef")
    public ElementSpecification addSubSpecification(final ElementSpecification specification)
    {
        if (specification instanceof MultiFormatElementSpecification)
        {
            return each(builder -> ((MultiFormatElementSpecification)specification).builders.stream()
                    .filter(builderToAppend -> builder.getClass() == builderToAppend.getClass())
                    .findFirst()
                    .map(builderToAppend -> builder.addSubSpecification(builderToAppend))
                    .orElse(INVALID));
        }
        return INVALID;
    }

    @Override
    public ElementSpecification thatContainsAnyElement()
    {
        return each(ElementSpecification::thatContainsAnyElement);
    }

    @Override
    public ElementSpecification withAttribute(final String attributeName)
    {
        return each(builder -> builder.withAttribute(attributeName));
    }

    @Override
    public ElementSpecification withoutAttribute(final String attributeName)
    {
        return each(builder -> builder.withoutAttribute(attributeName));
    }

    @Override
    public ElementSpecification thatContainsAChildOfType(final String tagName)
    {
        return each(builder -> builder.thatContainsAChildOfType(tagName));
    }

    @Override
    public ElementSpecification withClass(final String classname)
    {
        return each(builder -> builder.withClass(classname));
    }

    @Override
    public ElementSpecification withoutClass(final String classname)
    {
        return each(builder -> builder.withoutClass(classname));
    }

    @Override
    public ElementSpecification withAnyOfTheseClasses(final String... classnames)
    {
        return each(builder -> builder.withAnyOfTheseClasses(classnames));
    }

    @Override
    public ElementSpecification inPosition(final int position)
    {
        return each(builder -> builder.inPosition(position));
    }

    @Override
    public ElementSpecification withText(final String text)
    {
        return each(builder -> builder.withText(text));
    }

    @Override
    public ElementSpecification withTextContaining(final String text)
    {
        return each(builder -> builder.withTextContaining(text));
    }

    @Override
    public ElementSpecification withAttributeContaining(final String attributeName, final String expectedSubstring)
    {
        return each(builder -> builder.withAttributeContaining(attributeName, expectedSubstring));
    }

    @Override
    public ElementSpecification withAttributeValue(final String attributeName, final String value)
    {
        return each(builder -> builder.withAttributeValue(attributeName, value));
    }

    @Override
    public ElementSpecification withNumericalContent()
    {
        return each(ElementSpecification::withNumericalContent);
    }

    @Override
    public ElementSpecification withNoChildren()
    {
        return each(ElementSpecification::withNoChildren);
    }

    @Override
    public ElementSpecification thatIsChecked()
    {
        return each(ElementSpecification::thatIsChecked);
    }

    @Override
    public String asSeleniumLocator()
    {
        return getFirstValidBuilder().asSeleniumLocator();
    }

    @Override
    public boolean isValid()
    {
        return !builders.isEmpty();
    }

    @Override
    public By asWebDriverLocator()
    {
        return getFirstValidBuilder().asWebDriverLocator();
    }

    @Override
    public String toString()
    {
        return asSeleniumLocator();
    }

    private ElementSpecification getFirstValidBuilder()
    {
        return builders.stream()
                .filter(ElementSpecification::isValid)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No valid specification could be created."));
    }

    private ElementSpecification each(final UnaryOperator<ElementSpecification> operation)
    {
        return new MultiFormatElementSpecification(builders.stream()
                .map(operation)
                .filter(ElementSpecification::isValid)
                .collect(toList()));
    }
}

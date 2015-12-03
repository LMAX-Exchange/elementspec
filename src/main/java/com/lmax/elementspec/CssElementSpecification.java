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

import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.lmax.elementspec.InvalidElementSpecification.INVALID;
import static java.util.Optional.empty;

final class CssElementSpecification implements ElementSpecification
{
    private final Optional<CssToken> currentToken;
    private final String previousTokens;

    private CssElementSpecification(final String previousTokens, final CssToken currentToken)
    {
        this(previousTokens, Optional.of(currentToken));
    }

    private CssElementSpecification(final String previousTokens, final Optional<CssToken> currentToken)
    {
        this.previousTokens = previousTokens;
        this.currentToken = currentToken;
    }

    public static CssElementSpecification fromOldStyleSeleniumCssLocator(final String oldStyleSeleniumCssLocator)
    {
        return new CssElementSpecification(oldStyleSeleniumCssLocator, empty());
    }

    public static CssElementSpecification anElement()
    {
        return new CssElementSpecification("", CssToken.AN_ELEMENT);
    }

    public static CssElementSpecification anElementOfType(final String tagName)
    {
        return new CssElementSpecification("", CssToken.AN_ELEMENT.withTagName(tagName));
    }

    @Override
    public ElementSpecification withId(final String id)
    {
        if (!id.matches("^[A-Za-z\\-_]+$"))
        {
            return withAttributeValue("id", id);
        }
        else
        {
            return modifyCurrentToken(token -> token.withId(id));
        }
    }

    @Override
    public ElementSpecification thatContainsA(final String tagName)
    {
        return addNewToken(CssToken.AN_ELEMENT.withTagName(tagName));
    }

    @Override
    public ElementSpecification addSubSpecification(final ElementSpecification builder)
    {
        if (builder instanceof CssElementSpecification && builder.isValid())
        {
            return new CssElementSpecification(getCurrentCss() + " " + ((CssElementSpecification)builder).getCurrentCss(), empty());
        }
        return INVALID;
    }

    @Override
    public ElementSpecification thatContainsAnyElement()
    {
        return addNewToken(CssToken.AN_ELEMENT);
    }

    @Override
    public ElementSpecification withAttribute(final String attributeName)
    {
        return modifyCurrentToken(token -> token.withAttributeCondition(attributeName));
    }

    @Override
    public ElementSpecification withoutAttribute(final String attributeName)
    {
        return modifyCurrentToken(token -> token.withPseudoClass(":not([" + attributeName + "])"));
    }

    @Override
    public ElementSpecification withAttributeContaining(final String attributeName, final String expectedSubstring)
    {
        return modifyCurrentToken(token -> token.withAttributeCondition(attributeName + "*=\"" + expectedSubstring + "\""));
    }

    @Override
    public ElementSpecification withAttributeValue(final String attributeName, final String value)
    {
        return modifyCurrentToken(token -> token.withAttributeCondition(attributeName + "=\"" + value + "\""));
    }

    @Override
    public ElementSpecification thatContainsAChildOfType(final String tagName)
    {
        return addNewToken(CssToken.AN_ELEMENT.withRelationship(">").withTagName(tagName));
    }

    @Override
    public ElementSpecification withNoChildren()
    {
        return modifyCurrentToken(token -> token.withPseudoClass(":empty"));
    }

    @Override
    public ElementSpecification withClass(final String classname)
    {
        return modifyCurrentToken(token -> token.withClass(classname));
    }

    @Override
    public ElementSpecification withAnyOfTheseClasses(final String... classnames)
    {
        return INVALID;
    }

    @Override
    public ElementSpecification inPosition(final int position)
    {
        return modifyCurrentToken(token -> token.withPseudoClass(":nth-child(" + position + ")"));
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
    public ElementSpecification withNumericalContent()
    {
        return INVALID;
    }

    @Override
    public ElementSpecification thatIsChecked()
    {
        return modifyCurrentToken(token -> token.withPseudoClass(":checked"));
    }

    @Override
    public ElementSpecification withoutClass(final String classname)
    {
        return modifyCurrentToken(token -> token.withPseudoClass(":not(." + classname + ")"));
    }

    @Override
    public String asSeleniumLocator()
    {
        return "css=" + getCurrentCss();
    }

    @Override
    public By asWebDriverLocator()
    {
        return By.cssSelector(getCurrentCss());
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    private ElementSpecification modifyCurrentToken(final UnaryOperator<CssToken> operator)
    {
        return new CssElementSpecification(previousTokens, operator.apply(currentToken.orElse(CssToken.AN_ELEMENT)));
    }

    private ElementSpecification addNewToken(final CssToken cssToken)
    {
        return new CssElementSpecification(getCurrentCss(), cssToken);
    }

    private String getCurrentCss()
    {
        return currentToken
                .map(token -> previousTokens + " " + token.toString())
                .map(String::trim)
                .orElse(previousTokens);
    }
}

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


import static com.lmax.elementspec.CssToken.AN_ELEMENT;

public class CssTokenTest
{
    @Test
    public void shouldBeStarWhenNoOtherConditionsSpecified() throws Exception
    {
        assertCss(AN_ELEMENT, "*");
    }

    @Test
    public void shouldBeTagNameWhenOnlyTagNameIsSpecified() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input"), "input");
    }

    @Test
    public void shouldCreateSimpleClassSelector() throws Exception
    {
        assertCss(AN_ELEMENT.withClass("a"), ".a");
    }

    @Test
    public void shouldCreateMultipleClassSelectors() throws Exception
    {
        assertCss(AN_ELEMENT.withClass("a").withClass("b").withClass("c"), ".a.b.c");
    }

    @Test
    public void shouldCombineTagNameAndSingleClass() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input").withClass("aclass"), "input.aclass");
    }

    @Test
    public void shouldCombineTagNameAndMultipleClasses() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input").withClass("a").withClass("b").withClass("c"), "input.a.b.c");
    }

    @Test
    public void shouldCreateIdSelector() throws Exception
    {
        assertCss(AN_ELEMENT.withId("myid"), "#myid");
    }

    @Test
    public void shouldCombineTagNameAndId() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input").withId("myid"), "input#myid");
    }

    @Test
    public void shouldCombineTagNameIdAndMultipleClasses() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input").withId("myid").withClass("a").withClass("b").withClass("c"), "input#myid.a.b.c");
    }

    @Test
    public void shouldCreatePseudoClassSelector() throws Exception
    {
        assertCss(AN_ELEMENT.withPseudoClass(":nth-child(3)"), ":nth-child(3)");
    }

    @Test
    public void shouldCreateMultiplePseudoClassSelector() throws Exception
    {
        assertCss(AN_ELEMENT.withPseudoClass(":nth-child(3)").withPseudoClass(":checked"), ":nth-child(3):checked");
    }

    @Test
    public void shouldCombineTagNameAndMultiplePseudoClasses() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input").withPseudoClass(":nth-child(3)").withPseudoClass(":checked"), "input:nth-child(3):checked");
    }

    @Test
    public void shouldCombineTagNameIdMultipleClassesAndMultiplePseudoClasses() throws Exception
    {
        assertCss(AN_ELEMENT.withTagName("input").withId("foo").withClass("a").withClass("b").withPseudoClass(":nth-child(3)").withPseudoClass(":checked"), "input#foo.a.b:nth-child(3):checked");
    }

    @Test
    public void shouldCreateAttributeCondition() throws Exception
    {
        assertCss(AN_ELEMENT.withAttributeCondition("name"), "*[name]");
    }

    @Test
    public void shouldCreateMultipleAttributeConditions() throws Exception
    {
        assertCss(AN_ELEMENT.withAttributeCondition("name").withAttributeCondition("x=\"y\""), "*[name][x=\"y\"]");
    }

    @Test
    public void shouldUseRelationshipSelector() throws Exception
    {
        assertCss(AN_ELEMENT.withRelationship(">"), "> *");
    }

    @Test
    public void shouldCombineEverything() throws Exception
    {
        assertCss(AN_ELEMENT
                         .withRelationship("+")
                          .withTagName("input")
                          .withId("foo")
                          .withClass("a").withClass("b")
                          .withPseudoClass(":nth-child(3)").withPseudoClass(":checked")
                          .withAttributeCondition("name").withAttributeCondition("x=\"y\""),
                  "+ input#foo.a.b:nth-child(3):checked[name][x=\"y\"]");

    }

    private void assertCss(final CssToken token, final String expected)
    {
        assertThat(token.toString(), is(expected));
    }
}
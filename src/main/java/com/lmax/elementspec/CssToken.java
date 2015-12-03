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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;

final class CssToken
{
    static final CssToken AN_ELEMENT = new CssToken(" ", empty(), empty(), emptyList(), emptyList(), emptyList());

    private final String relationship;
    private final Optional<String> tagName;
    private final Optional<String> id;
    private final Collection<String> classNames;
    private final Collection<String> pseudoClasses;
    private final Collection<String> attributeConditions;

    private CssToken(final String relationship, final Optional<String> tagName, final Optional<String> id, final Collection<String> classNames, final Collection<String> pseudoClasses,
                     final Collection<String> attributeConditions)
    {
        this.relationship = relationship;
        this.tagName = tagName;
        this.id = id;
        this.classNames = classNames;
        this.pseudoClasses = pseudoClasses;
        this.attributeConditions = attributeConditions;
    }

    public CssToken withRelationship(final String relationship)
    {
        return new CssToken(relationship, tagName, id, classNames, pseudoClasses, attributeConditions);
    }

    public CssToken withTagName(final String tagName)
    {
        return new CssToken(relationship, Optional.of(tagName), id, classNames, pseudoClasses, attributeConditions);
    }

    public CssToken withClass(final String classname)
    {
        return new CssToken(relationship, tagName, id, addItem(classNames, classname), pseudoClasses, attributeConditions);
    }

    public CssToken withId(final String id)
    {
        return new CssToken(relationship, tagName, Optional.of(id), classNames, pseudoClasses, attributeConditions);
    }

    public CssToken withPseudoClass(final String pseudoClass)
    {
        return new CssToken(relationship, tagName, id, classNames, addItem(pseudoClasses, pseudoClass), attributeConditions);
    }

    public CssToken withAttributeCondition(final String condition)
    {
        return new CssToken(relationship, tagName, id, classNames, pseudoClasses, addItem(attributeConditions, condition));
    }

    public String toString()
    {
        final StringBuilder token = new StringBuilder();

        tagName.ifPresent(token::append);
        id.ifPresent(value -> token.append('#').append(value));
        token.append(classNames.stream()
                             .map(classname -> "." + classname)
                             .collect(joining()));
        token.append(pseudoClasses.stream().collect(joining()));

        if (token.length() == 0)
        {
            token.append("*");
        }

        token.append(attributeConditions.stream()
                             .map(condition -> "[" + condition + "]")
                             .collect(joining()));

        return (relationship + " " + token.toString()).trim();
    }

    private List<String> addItem(final Collection<String> existing, final String newValue)
    {
        final List<String> newValues = new ArrayList<>(existing);
        newValues.add(newValue);
        return newValues;
    }
}
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

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ElementSpecificationTest
{
    @Test
    public void shouldCopyElementSpecification()
    {
        final ElementSpecification base = ElementSpecification.anElementWithId("foo").thatContainsA("bar");
        final ElementSpecification copy = base.inPosition(0);
        assertThat(copy.toString(), not(base.toString()));
    }

    @Test
    public void shouldReturnACopyOnEachModification() throws Exception
    {
        final ElementSpecification baseSpec = ElementSpecification.anElementWithId("foo");
        final ElementSpecification withBarClass = baseSpec.withClass("bar");
        final ElementSpecification withDifferentChild = baseSpec.thatContainsA("different");
        assertThat(withBarClass.asSeleniumLocator(), not(withDifferentChild.asSeleniumLocator()));
        assertThat(withBarClass, not(withDifferentChild));
        assertThat(withBarClass, not(baseSpec));
    }
}

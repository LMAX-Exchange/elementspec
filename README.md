Element Specification
=====================
ElementSpecification is a small library that assists with writing automated UI tests.  It provides a simple, easy to read way to specify a particular element in the interface. For example identifying
a button to click to submit a form or identifying a table row to check the data presented is correct. Using ElementSpecification not only makes those selectors simpler to read, it automatically
avoids many of the most common mistakes that can lead to intermittent failures in tests.

## Maintainer

 * [Adrian Sutton](https://www.symphonious.net/)

## Quick Start

The exposed API is a single class `ElementSpecification` which provides a number of static methods as starting points for selecting elements. You can then refine the selection by chaining further
method calls. Using static imports is strongly recommended to avoid repeating `ElementSpecification.` for every selector.

<dl>
<dt><code>anElement()</code></dt>
<dd>Literally any element on the page.</dd>

<dt><code>anElementOfType(tagName)</code></dt>
<dd>Any element of the specified type, e.g. table.</dd>

<dt><code>anElementWithClass(classname)</code></dt>
<dd>Select an element with a given classname.  Equivalent to the css `.classname`.</dd>

<dt><code>anElementWithId(id)</code></dt>
<dd>Select an element by ID. Equivalent to the css `#id`.</dd>
</dl>


There are two categories of methods that can be used to refine the selection:

 * `with` methods add additional requirements for the current element. Most commonly used:
   * `withId(id)`
   * `withClass(classname)`
   * `withAttribute(attributeName)`
   * `withAttributeValue(attributeName, attributeValue)`
   * `withAttributeContaining(attributeName, attributeValueSubstring)`
 * `thatContains` methods move to a descendent element. Most commonly used:
   * `thatContainsA(tagName)`
   * `thatContainsAnElementWithClass(classname)`
   * `thatContainsAnElementWithId(id)`

Element specification selectors only ever move down the DOM tree, never back up or across. This is a deliberate restriction to keep selectors simple and reduce brittleness. More complex selectors
tend to depend on the layout of the page rather than the semantic structure leading to more brittle tests.

## Avoiding Intermittency

ElementSpecification will automatically avoid a number of potential causes of intermittency, for example ID attributes that happen to start with a number can fail to match CSS selectors in older
versions of FireFox so:

`anElementWithId("3ADF8")` is equivalent to `*[id="3ADF8"]` because `#3ADF8` would not match.

Similarly when using XPath, most people would translate `anElementWithClass("valid")` as `//*[contains(@class, 'valid')]` but that would also match elements with the classname `invalid`. Instead
ElementSpecification generates `contains(concat(' ', @class, ' '), ' valid ')`.

There are a number of little details like that built into ElementSpecification which avoid the need for every person on your team to be constantly aware of every possible pitfall while writing tests.

## Examples

Note that generally the CSS version is *shorter* but the ElementSpecification syntax aims to be *clearer* and readable without having to recall all the details of CSS.

`anElementWithId("login-form").thatContainsAnElementWithClass("username")`  is equivalent to `#login-form .username`

`anElementWithId("data-table").thatContainsA("tr").withAttributeValue("data-id", "78").thatContainsAnElementWithClass("name")` is equivalent to `#data-table tr[data-id='78'] .name`

`anElement().withAnyOfTheseClasses("buy-price", "sell-price")` is impossible in CSS so ElementSpecification automatically switches to XPath and generates:<br>
`//*[contains(concat(' ', @class, ' '), ' buy-price ') or contains(concat(' ', @class, ' '), ' sell-price ')]`

## Output Formats

ElementSpecification will automatically select the simplest (and generally fastest) format that a selector can be accurately represented in.
The three available formats (in order of preference) are:

 * Simple ID (ie: `By.id`)
 * CSS
 * XPath


The selector can be generated as either a selenium 1 style locator (`String`) using `asSeleniumLocator` or a WebDriver locator (`By`) using `asWebDriverLocator`.
/*
 * SonarQube CSS Plugin
 * Copyright (C) 2013-2016 Tamas Kende and David RACODON
 * mailto: kende.tamas@gmail.com and david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.css.parser;

import org.junit.Test;
import org.sonar.plugins.css.api.tree.RulesetTree;

import static org.fest.assertions.Assertions.assertThat;

public class RulesetTreeTest extends TreeTest {

  public RulesetTreeTest() {
    super(CssLexicalGrammar.RULESET);
  }

  @Test
  public void ruleset() {
    RulesetTree tree;

    tree = checkParsed("{}");
    assertThat(tree.selectors()).isNull();

    tree = checkParsed(" {}");
    assertThat(tree.selectors()).isNull();

    tree = checkParsed(" { }");
    assertThat(tree.selectors()).isNull();

    tree = checkParsed("{color:green}");
    assertThat(tree.selectors()).isNull();

    checkParsed("{color:green;}");
    checkParsed("{color:green;;}");
    checkParsed("{;color:green;}");
    checkParsed("{;}");
    checkParsed("{;;}");
    checkParsed("{;;;}");
    checkParsed("{ ; ; ; }");

    checkParsed("{ color: green !important; }");
    checkParsed("{ color: !important green }");

    checkParsed("{ color: min() }");

    tree = checkParsed("h1,h2{}");
    assertThat(tree.selectors()).isNotNull();

    tree = checkParsed(" h1,h2{}");
    assertThat(tree.selectors()).isNotNull();

    tree = checkParsed(" h1, h2 { }");
    assertThat(tree.selectors()).isNotNull();

    tree = checkParsed(" h1,h2{}");
    assertThat(tree.selectors()).isNotNull();
  }

  @Test
  public void notRuleset() {
    checkNotParsed("{color}");
    checkNotParsed("{color:}");
    checkNotParsed("h1,h2");
    checkNotParsed("h1,h2{");
    checkNotParsed("{ !important }");
    checkNotParsed("{ ab() }");
  }

  private RulesetTree checkParsed(String toParse) {
    RulesetTree tree = (RulesetTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.block()).isNotNull();
    return tree;
  }

}

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
import org.sonar.plugins.css.api.tree.SyntaxToken;

import static org.fest.assertions.Assertions.assertThat;

public class BOMTreeTest extends TreeTest {

  public BOMTreeTest() {
    super(CssLexicalGrammar.BOM);
  }

  @Test
  public void bom() {
    checkParsed("\ufeff");
  }

  @Test
  public void notBom() {
    checkNotParsed("\"\\ufeff\"");
  }

  private void checkParsed(String toParse) {
    SyntaxToken tree = (SyntaxToken) parser().parse(toParse);
    assertThat(tree.text()).isEqualTo("\ufeff");
  }

}

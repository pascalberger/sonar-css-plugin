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
package org.sonar.css.checks;

import org.junit.Test;
import org.sonar.css.checks.verifier.CssCheckVerifier;

import static org.fest.assertions.Assertions.assertThat;

public class SelectorNamingConventionCheckTest {

  private final SelectorNamingConventionCheck check = new SelectorNamingConventionCheck();

  @Test
  public void test_with_default_format() {
    check.setFormat("^[a-z][-a-z0-9]*$");
    CssCheckVerifier.verify(check, CheckTestUtils.getCssTestFile("selectorNamingConvention.css"));
  }

  @Test
  public void test_with_custom_format() {
    check.setFormat("^[-a-z]+$");
    CssCheckVerifier.verify(check, CheckTestUtils.getCssTestFile("selectorNamingConventionCustomFormat.css"));
  }

  @Test
  public void should_throw_an_illegal_state_exception_as_the_format_parameter_is_not_valid() {
    try {
      check.setFormat("(");

      CssCheckVerifier.issues(check, CheckTestUtils.getCssTestFile("selectorNamingConvention.css")).noMore();

    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).isEqualTo("Check css:selector-naming-convention (Selectors should follow a naming convention): "
        + "format parameter \"(\" is not a valid regular expression.");
    }
  }

}

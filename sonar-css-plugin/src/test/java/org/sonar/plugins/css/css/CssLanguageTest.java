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
package org.sonar.plugins.css.css;

import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.css.css.CssLanguage;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CssLanguageTest {

  @Test
  public void language_key_and_name() {
    CssLanguage css = new CssLanguage(mock(Settings.class));
    assertThat(css.getKey()).isEqualTo("css");
    assertThat(css.getName()).isEqualTo("CSS");
  }

  @Test
  public void default_file_suffix() {
    CssLanguage css = new CssLanguage(mock(Settings.class));
    assertThat(css.getFileSuffixes()).containsOnly("css");
  }

  @Test
  public void custom_file_suffixes() {
    Settings settings = new Settings();
    settings.setProperty("sonar.css.file.suffixes", "css,css3");

    CssLanguage css = new CssLanguage(settings);
    assertThat(css.getFileSuffixes()).containsOnly("css", "css3");
  }

}

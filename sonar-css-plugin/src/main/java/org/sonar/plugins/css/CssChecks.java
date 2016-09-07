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
package org.sonar.plugins.css;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.css.api.CssCheck;
import org.sonar.plugins.css.api.CustomRulesDefinition;
import org.sonar.plugins.css.api.visitors.TreeVisitor;

public class CssChecks {

  private final CheckFactory checkFactory;
  private Set<Checks<CssCheck>> checksByRepository = Sets.newHashSet();

  private CssChecks(CheckFactory checkFactory) {
    this.checkFactory = checkFactory;
  }

  public static CssChecks createCssChecks(CheckFactory checkFactory) {
    return new CssChecks(checkFactory);
  }

  public CssChecks addChecks(String repositoryKey, Iterable<Class> checkClass) {
    checksByRepository.add(checkFactory
      .<CssCheck>create(repositoryKey)
      .addAnnotatedChecks(checkClass));

    return this;
  }

  public CssChecks addCustomChecks(@Nullable CustomRulesDefinition[] customRulesDefinitions) {
    if (customRulesDefinitions != null) {

      for (CustomRulesDefinition rulesDefinition : customRulesDefinitions) {
        addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
      }
    }
    return this;
  }

  public List<CssCheck> all() {
    List<CssCheck> allVisitors = Lists.newArrayList();

    for (Checks<CssCheck> checks : checksByRepository) {
      allVisitors.addAll(checks.all());
    }

    return allVisitors;
  }

  public List<TreeVisitor> visitorChecks() {
    return all().stream()
      .filter(c -> c instanceof TreeVisitor)
      .map(c -> (TreeVisitor) c)
      .collect(Collectors.toList());
  }

  @Nullable
  public RuleKey ruleKeyFor(CssCheck check) {
    RuleKey ruleKey;

    for (Checks<CssCheck> checks : checksByRepository) {
      ruleKey = checks.ruleKey(check);

      if (ruleKey != null) {
        return ruleKey;
      }
    }
    return null;
  }

}

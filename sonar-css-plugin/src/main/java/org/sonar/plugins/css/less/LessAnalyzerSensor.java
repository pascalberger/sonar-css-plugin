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
package org.sonar.plugins.css.less;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.css.checks.CheckList;
import org.sonar.css.visitors.cpd.CpdVisitor;
import org.sonar.css.visitors.highlighter.SyntaxHighlighterVisitor;
import org.sonar.css.visitors.metrics.MetricsVisitor;
import org.sonar.plugins.css.AbstractLanguageAnalyzerSensor;
import org.sonar.plugins.css.CssChecks;
import org.sonar.plugins.css.api.CustomLessRulesDefinition;
import org.sonar.plugins.css.api.CustomRulesDefinition;
import org.sonar.plugins.css.api.visitors.TreeVisitor;

public class LessAnalyzerSensor extends AbstractLanguageAnalyzerSensor {

  public LessAnalyzerSensor(FileSystem fileSystem, CheckFactory checkFactory, NoSonarFilter noSonarFilter) {
    super(fileSystem, checkFactory, noSonarFilter);
  }

  public LessAnalyzerSensor(FileSystem fileSystem, CheckFactory checkFactory, NoSonarFilter noSonarFilter,
    @Nullable CustomLessRulesDefinition[] customRulesDefinition) {
    super(fileSystem, checkFactory, noSonarFilter, customRulesDefinition);
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
      .onlyOnLanguage(LessLanguage.KEY)
      .name("Less Analyzer")
      .onlyOnFileType(Type.MAIN);
  }

  public String languageToAnalyze() {
    return LessLanguage.NAME;
  }

  @Override
  public List<InputFile> filesToAnalyze(FileSystem fileSystem) {
    return StreamSupport.stream(fileSystem.inputFiles(mainFilePredicate(fileSystem)).spliterator(), false)
      .collect(Collectors.toList());
  }

  @Override
  public CssChecks checks(CheckFactory checkFactory, CustomRulesDefinition[] customRulesDefinitions) {
    return CssChecks.createCssChecks(checkFactory)
      .addChecks(CheckList.LESS_REPOSITORY_KEY, CheckList.getLessChecks())
      .addCustomChecks(customRulesDefinitions);
  }

  @Override
  public List<TreeVisitor> treeVisitors(SensorContext sensorContext, CssChecks checks, NoSonarFilter noSonarFilter) {
    List<TreeVisitor> treeVisitors = Lists.newArrayList();
    treeVisitors.addAll(checks.visitorChecks());
    treeVisitors.add(new SyntaxHighlighterVisitor(sensorContext));
    treeVisitors.add(new CpdVisitor(sensorContext));
    treeVisitors.add(new MetricsVisitor(sensorContext, noSonarFilter));
    return treeVisitors;
  }

  private FilePredicate mainFilePredicate(FileSystem fileSystem) {
    return fileSystem.predicates()
      .and(
        fileSystem.predicates().hasType(InputFile.Type.MAIN),
        fileSystem.predicates().hasLanguage(LessLanguage.KEY));
  }

}

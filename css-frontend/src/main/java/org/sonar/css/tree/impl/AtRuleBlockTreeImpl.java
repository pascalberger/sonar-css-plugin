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
package org.sonar.css.tree.impl;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import org.sonar.plugins.css.api.tree.*;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitor;

public class AtRuleBlockTreeImpl extends CssTree implements AtRuleBlockTree {

  private final SyntaxToken openCurlyBrace;
  private final SyntaxToken closeCurlyBrace;
  private final List<Tree> content;
  private final List<PropertyDeclarationTree> propertyDeclarations;
  private final List<VariableDeclarationTree> variableDeclarations;
  private final List<AtRuleTree> atRules;
  private final List<RulesetTree> rulesets;

  public AtRuleBlockTreeImpl(SyntaxToken openCurlyBrace, @Nullable List<Tree> content, SyntaxToken closeCurlyBrace) {
    this.openCurlyBrace = openCurlyBrace;
    this.closeCurlyBrace = closeCurlyBrace;
    this.content = content;

    propertyDeclarations = new ArrayList<>();
    variableDeclarations = new ArrayList<>();
    atRules = new ArrayList<>();
    rulesets = new ArrayList<>();
    buildLists(content);
  }

  @Override
  public Kind getKind() {
    return Kind.AT_RULE_BLOCK;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    if (content != null) {
      return Iterators.concat(
        Iterators.singletonIterator(openCurlyBrace),
        content.iterator(),
        Iterators.singletonIterator(closeCurlyBrace));
    } else {
      return Iterators.forArray(openCurlyBrace, closeCurlyBrace);
    }
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitAtRuleBlock(this);
  }

  @Override
  public SyntaxToken openCurlyBrace() {
    return openCurlyBrace;
  }

  @Override
  public SyntaxToken closeCurlyBrace() {
    return closeCurlyBrace;
  }

  @Override
  public List<PropertyDeclarationTree> propertyDeclarations() {
    return propertyDeclarations;
  }

  @Override
  public List<VariableDeclarationTree> variableDeclarations() {
    return variableDeclarations;
  }

  @Override
  public List<AtRuleTree> atRules() {
    return atRules;
  }

  @Override
  @Nullable
  public List<Tree> content() {
    return content;
  }

  @Override
  public List<RulesetTree> rulesets() {
    return rulesets;
  }

  private void buildLists(@Nullable List<Tree> content) {
    if (content != null) {
      for (Tree tree : content) {
        if (tree instanceof AtRuleTree) {
          atRules.add((AtRuleTree) tree);
        } else if (tree instanceof RulesetTree) {
          rulesets.add((RulesetTree) tree);
        } else if (tree instanceof DeclarationsTree) {
          propertyDeclarations.addAll(((DeclarationsTree) tree).propertyDeclarations());
          variableDeclarations.addAll(((DeclarationsTree) tree).variableDeclarations());
        }
      }
    }
  }

}

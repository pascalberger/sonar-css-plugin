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
package org.sonar.plugins.css.api.visitors;

import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import org.sonar.css.tree.impl.CssTree;
import org.sonar.plugins.css.api.tree.*;

public abstract class DoubleDispatchVisitor implements TreeVisitor {

  private TreeVisitorContext context = null;

  @Override
  public TreeVisitorContext getContext() {
    Preconditions.checkState(context != null, "this#scanTree(context) should be called to initialised the context before accessing it");
    return context;
  }

  @Override
  public final void scanTree(TreeVisitorContext context) {
    this.context = context;
    scan(context.getTopTree());
  }

  protected void scan(@Nullable Tree tree) {
    if (tree != null) {
      tree.accept(this);
    }
  }

  protected void scanChildren(Tree tree) {
    Iterator<Tree> childrenIterator = ((CssTree) tree).childrenIterator();

    Tree child;

    while (childrenIterator.hasNext()) {
      child = childrenIterator.next();
      if (child != null) {
        child.accept(this);
      }
    }
  }

  protected <T extends Tree> void scan(List<T> trees) {
    trees.forEach(this::scan);
  }

  public void visitStyleSheet(StyleSheetTree tree) {
    scanChildren(tree);
  }

  public void visitAtRule(AtRuleTree tree) {
    scanChildren(tree);
  }

  public void visitRuleset(RulesetTree tree) {
    scanChildren(tree);
  }

  public void visitAtRuleBlock(AtRuleBlockTree tree) {
    scanChildren(tree);
  }

  public void visitRulesetBlock(RulesetBlockTree tree) {
    scanChildren(tree);
  }

  public void visitParenthesisBlock(ParenthesisBlockTree tree) {
    scanChildren(tree);
  }

  public void visitBracketBlock(BracketBlockTree tree) {
    scanChildren(tree);
  }

  public void visitDeclarations(DeclarationsTree tree) {
    scanChildren(tree);
  }

  public void visitPropertyDeclaration(PropertyDeclarationTree tree) {
    scanChildren(tree);
  }

  public void visitVariableDeclaration(VariableDeclarationTree tree) {
    scanChildren(tree);
  }

  public void visitProperty(PropertyTree tree) {
    scanChildren(tree);
  }

  public void visitValue(ValueTree tree) {
    scanChildren(tree);
  }

  public void visitFunction(FunctionTree tree) {
    scanChildren(tree);
  }

  public void visitUri(UriTree tree) {
    scanChildren(tree);
  }

  public void visitUriContent(UriContentTree tree) {
    scanChildren(tree);
  }

  public void visitSelectors(SelectorsTree tree) {
    scanChildren(tree);
  }

  public void visitSelector(SelectorTree tree) {
    scanChildren(tree);
  }

  public void visitCompoundSelector(CompoundSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitClassSelector(ClassSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitIdSelector(IdSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitPseudoSelector(PseudoSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitKeyframesSelector(KeyframesSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitTypeSelector(TypeSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitAttributeSelector(AttributeSelectorTree tree) {
    scanChildren(tree);
  }

  public void visitAttributeMatcherExpression(AttributeMatcherExpressionTree tree) {
    scanChildren(tree);
  }

  public void visitAttributeMatcher(AttributeMatcherTree tree) {
    scanChildren(tree);
  }

  public void visitSelectorCombinator(SelectorCombinatorTree tree) {
    scanChildren(tree);
  }

  public void visitPseudoFunction(PseudoFunctionTree tree) {
    scanChildren(tree);
  }

  public void visitPseudoIdentifier(PseudoIdentifierTree tree) {
    scanChildren(tree);
  }

  public void visitNamespace(NamespaceTree tree) {
    scanChildren(tree);
  }

  public void visitImportant(ImportantTree tree) {
    scanChildren(tree);
  }

  public void visitAtKeyword(AtKeywordTree tree) {
    scanChildren(tree);
  }

  public void visitHash(HashTree tree) {
    scanChildren(tree);
  }

  public void visitPercentage(PercentageTree tree) {
    scanChildren(tree);
  }

  public void visitDimension(DimensionTree tree) {
    scanChildren(tree);
  }

  public void visitVariable(VariableTree tree) {
    scanChildren(tree);
  }

  public void visitUnit(UnitTree tree) {
    scanChildren(tree);
  }

  public void visitIdentifier(IdentifierTree tree) {
    scanChildren(tree);
  }

  public void visitString(StringTree tree) {
    scanChildren(tree);
  }

  public void visitUnicodeRange(UnicodeRangeTree tree) {
    scanChildren(tree);
  }

  public void visitNumber(NumberTree tree) {
    scanChildren(tree);
  }

  public void visitCaseInsensitiveFlag(CaseInsensitiveFlagTree tree) {
    scanChildren(tree);
  }

  public void visitDelimiter(DelimiterTree tree) {
    scanChildren(tree);
  }

  public void visitToken(SyntaxToken token) {
    for (SyntaxTrivia syntaxTrivia : token.trivias()) {
      syntaxTrivia.accept(this);
    }
  }

  public void visitComment(SyntaxTrivia commentToken) {
    // no sub-tree
  }

}

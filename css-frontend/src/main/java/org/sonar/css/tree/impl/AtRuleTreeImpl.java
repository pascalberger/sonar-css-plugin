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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

import org.sonar.css.model.Vendor;
import org.sonar.css.model.atrule.StandardAtRule;
import org.sonar.css.model.atrule.StandardAtRuleFactory;
import org.sonar.plugins.css.api.tree.*;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitor;

public class AtRuleTreeImpl extends CssTree implements AtRuleTree {

  private final AtKeywordTree atKeyword;
  private final List<Tree> preludes;
  private final AtRuleBlockTree block;
  private final SyntaxToken semicolon;
  private final StandardAtRule standardAtRule;
  private final Vendor vendor;

  public AtRuleTreeImpl(AtKeywordTree atKeyword, @Nullable List<Tree> preludes, @Nullable AtRuleBlockTree block, @Nullable SyntaxToken semicolon) {
    this.atKeyword = atKeyword;
    this.preludes = preludes;
    this.block = block;
    this.semicolon = semicolon;
    this.vendor = setVendor();
    this.standardAtRule = setStandardAtRule();
  }

  @Override
  public Kind getKind() {
    return Kind.AT_RULE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    if (preludes != null) {
      return Iterators.concat(
        Iterators.singletonIterator(atKeyword),
        preludes.iterator(),
        Iterators.forArray(block, semicolon));
    } else {
      return Iterators.forArray(atKeyword, block);
    }
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitAtRule(this);
  }

  @Override
  public AtKeywordTree atKeyword() {
    return atKeyword;
  }

  @Override
  @Nullable
  public List<Tree> preludes() {
    return preludes;
  }

  @Override
  @Nullable
  public AtRuleBlockTree block() {
    return block;
  }

  @Override
  @Nullable
  public SyntaxToken semicolon() {
    return semicolon;
  }

  @Override
  public boolean isVendorPrefixed() {
    return vendor != null;
  }

  @Override
  @Nullable
  public Vendor vendor() {
    return vendor;
  }

  @Override
  public StandardAtRule standardAtRule() {
    return standardAtRule;
  }

  private Vendor setVendor() {
    for (Vendor v : Vendor.values()) {
      if (atKeyword.keyword().text().toLowerCase(Locale.ENGLISH).startsWith(v.getPrefix())) {
        return v;
      }
    }
    return null;
  }

  private StandardAtRule setStandardAtRule() {
    String atRuleName = atKeyword.keyword().text();
    if (isVendorPrefixed()) {
      atRuleName = atRuleName.substring(vendor.getPrefix().length());
    }
    return StandardAtRuleFactory.getByName(atRuleName);
  }

}

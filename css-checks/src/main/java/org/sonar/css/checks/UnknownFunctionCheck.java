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

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.css.model.function.UnknownFunction;
import org.sonar.plugins.css.api.tree.FunctionTree;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "unknown-functions",
  name = "Unknown functions should be removed",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class UnknownFunctionCheck extends DoubleDispatchVisitorCheck {

  @Override
  public void visitFunction(FunctionTree tree) {
    if (tree.standardFunction() instanceof UnknownFunction && !tree.isVendorPrefixed()) {
      addPreciseIssue(
        tree.function(),
        "Remove this usage of the unknown \"" + tree.standardFunction().getName() + "\" function.");
    }
    super.visitFunction(tree);
  }

}

/*
 * SonarQube Java
 * Copyright (C) 2012-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
package org.sonar.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.RspecKey;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "EmptyStatementUsageCheck")
@RspecKey("S1116")
public class EmptyStatementUsageCheck extends IssuableSubscriptionVisitor {

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.EMPTY_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    if (usedForEmptyEnum(tree) || uniqueStatementOfLoop(tree)) {
      return;
    }
    reportIssue(tree, "Remove this empty statement.");
  }

  private static boolean usedForEmptyEnum(Tree tree) {
    Tree parent = tree.parent();
    if (parent.is(Tree.Kind.ENUM)) {
      return ((ClassTree) parent).members().indexOf(tree) == 0;
    }
    return false;
  }

  private static boolean uniqueStatementOfLoop(Tree tree) {
    return tree.parent().is(Tree.Kind.WHILE_STATEMENT, Tree.Kind.FOR_EACH_STATEMENT, Tree.Kind.FOR_STATEMENT, Tree.Kind.DO_STATEMENT);
  }
}

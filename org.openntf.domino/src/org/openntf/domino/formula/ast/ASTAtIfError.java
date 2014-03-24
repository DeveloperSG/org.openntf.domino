/* Generated By:JJTree: Do not edit this line. ASTAtIfError.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.formula.ast;

import org.openntf.domino.formula.AtFormulaParser;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public class ASTAtIfError extends SimpleNode {
	public ASTAtIfError(final int id) {
		super(id);
	}

	public ASTAtIfError(final AtFormulaParser p, final int id) {
		super(p, id);
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws EvaluateException {
		try {
			children[0].evaluate(ctx);
		} catch (RuntimeException ex) {
			if (children.length == 2) {
				return children[0].evaluate(ctx);
			}
		}
		return ValueHolder.valueDefault();
	}

	public void toFormula(final StringBuilder sb) {
		sb.append("@IfError");
		appendParams(sb);
	}
}
/* JavaCC - OriginalChecksum=776aeb60d75aec0e2d82f5d09d2401a6 (do not edit this line) */

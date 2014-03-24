/* Generated By:JJTree: Do not edit this line. ASTValueString.java Version 4.3 */
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
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public class ASTValueString extends SimpleNode {

	private ValueHolder value;

	public ASTValueString(final int id) {
		super(id);
	}

	public ASTValueString(final AtFormulaParser p, final int id) {
		super(p, id);
	}

	public void parseString(String image, final char c) {
		image = image.substring(1, image.length() - 1);
		if (c == '{') {
			// fertig
		} else if (c == '"') {
			int pos = 0;
			int start = 0;
			// YES: This looks complicated. But we want to be as much compatible as possible
			if ((pos = image.indexOf('\\')) != -1) {
				StringBuffer sb = new StringBuffer(image.substring(start, pos));
				while (true) {
					start = pos + 1;
					pos = image.indexOf('\\', start);
					if (pos == -1) {
						sb.append(image.substring(start));
						break;
					}
					sb.append(image.substring(start, pos));
				}
				image = sb.toString();
			}
		}
		value = ValueHolder.valueOf(image);
	}

	@Override
	public String toString() {
		return super.toString() + ": " + value.getString(0);
	}

	@Override
	public void toFormula(final StringBuilder sb) {
		// TODO Quote Stings properly!
		String s = value.getString(0).replace("\\", "\\\\");
		s = s.replace("\"", "\\\"");
		sb.append("\"" + s + "\"");
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) {
		return value;
	}

}
/* JavaCC - OriginalChecksum=bbf33130d016e7ec065687da9b85c8fd (do not edit this line) */

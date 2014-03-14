/* Generated By:JJTree: Do not edit this line. ASTValueDateOrKW.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.openntf.domino.tests.rpr.formula.parse;

import org.openntf.domino.tests.rpr.formula.eval.FormulaContext;
import org.openntf.domino.tests.rpr.formula.eval.ValueHolder;

public class ASTValueDateOrKW extends SimpleNode {
	Object value = null;

	public ASTValueDateOrKW(final int id) {
		super(id);
	}

	public ASTValueDateOrKW(final AtFormulaParser p, final int id) {
		super(p, id);
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) {
		return new ValueHolder(value);
	}

	public void init(final String image) throws ParseException {
		String inner = image.substring(1, image.length() - 1); // remove first [ and last ]
		if (inner.contains(".") || inner.contains("/") || inner.contains("-")) {
			// this MUST be a date
			value = parser.getFormatter().parseDate(parser, inner);
		} else {
			// This is a Date OR a Keyword.... we don't know.
			try {
				value = parser.getFormatter().parseDate(parser, inner);
			} catch (ParseException e) {
				value = image; // tried to parse. but this seems to be a Keyword
			}
		}
	}

	@Override
	public void toFormula(final StringBuilder sb) {
		sb.append(value);
	}

}
/* JavaCC - OriginalChecksum=7c2e0ea0c4e20b21c6188bf4e28c7fe3 (do not edit this line) */
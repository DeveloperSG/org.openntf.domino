/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.AbstractList;
import java.util.logging.Logger;

import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

/**
 * @author jgallagher
 * 
 */
public class FormFieldList extends AbstractList<FormField> implements org.openntf.domino.design.FormFieldList {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FormFieldList.class.getName());

	private final DesignForm parent_;
	private final String pattern_;
	private XMLNodeList nodes_;

	protected FormFieldList(final DesignForm parent, final String pattern) {
		parent_ = parent;
		pattern_ = pattern;
		refreshNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public FormField get(int index) {
		return new FormField(nodes_.get(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return nodes_.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public FormField remove(int index) {
		FormField current = get(index);
		nodes_.remove(index);
		return current;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		if (!(o instanceof FormField || o instanceof String)) {
			throw new IllegalArgumentException();
		}
		String name = o instanceof String ? o.toString() : ((FormField) o).getName();
		for (int i = 0; i < size(); i++) {
			if (name.equalsIgnoreCase(get(i).getName())) {
				remove(i);
				return true;
			}
		}

		return false;
	}

	public FormField addField() {
		try {
			XMLNode body = parent_.getDxl().selectSingleNode("/form/body/richtext");

			// Create an appropriate paragraph definition
			XMLNode finalPardef = parent_.getDxl().selectSingleNode("//pardef[last()]");
			int nextId = Integer.valueOf(finalPardef.getAttribute("id")) + 1;
			XMLNode pardef = body.addChildElement("pardef");
			pardef.setAttribute("id", String.valueOf(nextId));
			pardef.setAttribute("hide", "notes web mobile");

			// Now create the par and the field
			XMLNode par = body.addChildElement("par");
			par.setAttribute("def", pardef.getAttribute("id"));

			// Now add the field
			XMLNode field = par.addChildElement("field");
			field.setAttribute("kind", "editable");
			field.setAttribute("name", "");
			field.setAttribute("type", "text");

			refreshNodes();

			return new FormField(field);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void swap(final int a, final int b) {
		try {
			XMLNodeList fieldNodes = (XMLNodeList) parent_.getDxl().selectNodes("//field");
			fieldNodes.swap(a, b);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}

	private void refreshNodes() {
		try {
			nodes_ = (XMLNodeList) parent_.getDxl().selectNodes(pattern_);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}
}

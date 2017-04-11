package org.openntf.domino.graph2.builtin.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.helpers.DocumentScanner;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(IndexDatabase.VALUE_FORM_NAME)
@JavaHandlerClass(Value.ValueImpl.class)
public interface Value extends DVertexFrame {
	public static enum Utils {
		;
		public static void processValue(final Value value, final DFramedTransactionalGraph graph, final boolean caseSensitive,
				final boolean commit) {
			Boolean processed = value.isTokenProcessed();
			if (processed == null || !processed) {
				String val = value.getValue();
				Scanner s = new Scanner(val);
				s.useDelimiter(DocumentScanner.REGEX_NONALPHANUMERIC);
				while (s.hasNext()) {
					CharSequence token = DocumentScanner.scrubToken(s.next(), caseSensitive);
					if (token != null && (token.length() > 2)) {
						Term tokenV = (Term) graph.addVertex(token.toString().toLowerCase(), Term.class);
						if (tokenV.getValue() == null || tokenV.getValue().length() == 0) {
							tokenV.setValue(token.toString());
						}
						value.addTerm(tokenV);
					}
				}
				value.setTokenProcessed(true);
				if (commit) {
					graph.commit();
				}
			}
		}
	}

	@TypeValue(ContainsTerm.LABEL)
	public static interface ContainsTerm extends DEdgeFrame {
		public static final String LABEL = "ContainsTerm";

		@InVertex
		public Value getValue();

		@OutVertex
		public Term getTerm();
	}

	@TypedProperty("value")
	public String getValue();

	@TypedProperty("value")
	public void setValue(String value);

	@TypedProperty("isTokenProcessed")
	public Boolean isTokenProcessed();

	@TypedProperty("isTokenProcessed")
	public void setTokenProcessed(boolean processed);

	@JavaHandler
	@TypedProperty("Hits")
	public Map getHits();

	@JavaHandler
	@TypedProperty("HitRepls")
	public List<CharSequence> getHitRepls();

	@AdjacencyUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public Iterable<Term> getTerms();

	@AdjacencyUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public ContainsTerm addTerm(Term term);

	@AdjacencyUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public void removeTerm(Term term);

	@IncidenceUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public Iterable<ContainsTerm> getContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public int countContainsTerms();

	@IncidenceUnique(label = ContainsTerm.LABEL, direction = Direction.IN)
	public void removeContainsTerm(ContainsTerm containsTerm);

	public static abstract class ValueImpl extends DVertexFrameImpl implements Value, JavaHandlerContext<Vertex> {

		@Override
		public Map getHits() {
			Map<CharSequence, Object> result = new LinkedHashMap<CharSequence, Object>();
			for (CharSequence replid : getHitRepls()) {
				String itemname = IndexDatabase.VALUE_MAP_PREFIX + replid;
				Vertex v = asVertex();
				Object raw = v.getProperty(itemname);
				//				System.out.println("TEMP DEBUG getting value location map from item " + itemname + " from vertex "
				//						+ String.valueOf(v.getId()) + " (id: " + System.identityHashCode(raw) + ")");
				if (raw instanceof Map) {
					Map m = (Map) raw;
					for (Object key : m.keySet()) {
						//						System.out.println("TEMP DEBUG " + String.valueOf(key) + ":" + String.valueOf(m.get(key)) + " ("
						//								+ m.get(key).getClass().getName() + ")");
					}
				}
				result.put(replid, this.asVertex().getProperty(itemname));
			}
			return result;
		}

		@Override
		public List<CharSequence> getHitRepls() {
			//			System.out.println("TEMP DEBUG getting value hit repl list");
			List<CharSequence> result = new ArrayList<CharSequence>();
			Map<CharSequence, Object> map = this.asMap();
			for (CharSequence cs : map.keySet()) {
				String str = cs.toString();
				if (str.startsWith(IndexDatabase.VALUE_MAP_PREFIX)) {
					result.add(str.substring(IndexDatabase.VALUE_MAP_PREFIX.length()));
				}
			}
			return result;
		}

	}

}

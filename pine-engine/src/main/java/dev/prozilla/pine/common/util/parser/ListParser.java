package dev.prozilla.pine.common.util.parser;

import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser that parses a list of elements using another parser.
 * @param <E> The type of elements in the result
 */
public class ListParser<E> extends Parser<List<E>> {
	
	/** Parser used to parse elements of a list. */
	protected Parser<E> elementParser;
	/** The symbol that separates elements in an input string. */
	protected String separator;
	/** The policy that determines how input elements are handled that the {@code elementParser} fails to parse. */
	protected ElementParsingFailPolicy policy;
	
	public static final String DEFAULT_SEPARATOR = ",";
	public static final ElementParsingFailPolicy DEFAULT_POLICY = ElementParsingFailPolicy.IGNORE;
	
	public ListParser(Parser<E> elementParser) {
		this(elementParser, DEFAULT_SEPARATOR);
	}
	
	public ListParser(Parser<E> elementParser, String separator) {
		this(elementParser, separator, DEFAULT_POLICY);
	}
	
	public ListParser(Parser<E> elementParser, String separator, ElementParsingFailPolicy policy) {
		this.elementParser = Checks.isNotNull(elementParser, "elementParser");
		this.separator = Checks.isNotNull(separator, "separator");
		this.policy = Checks.isNotNull(policy, "policy");
	}
	
	@Override
	public boolean parse(String input) {
		String[] inputElements = input.split(getRegex());
		List<E> result = createList();
		
		for (String inputElement : inputElements) {
			if (elementParser.parse(inputElement) || policy == ElementParsingFailPolicy.REPLACE_WITH_NULL) {
				result.add(elementParser.getResult());
			} else if (policy == ElementParsingFailPolicy.FAIL) {
				return fail("Failed to parse element: " + elementParser.getError());
			}
		}
		
		return succeed(result);
	}
	
	/**
	 * Creates the initial list used to compose the parsing result.
	 * @return The initial list
	 */
	protected List<E> createList() {
		return new ArrayList<>();
	}
	
	/**
	 * Returns the regex based on the separator of this parser.
	 * @return The regex based on the separator
	 */
	protected String getRegex() {
		return Pattern.quote(separator);
	}
	
	public void setElementParser(Parser<E> elementParser) {
		this.elementParser = Checks.isNotNull(elementParser, "elementParser");
	}
	
	public void setSeparator(String separator) {
		this.separator = Checks.isNotNull(separator, "separator");
	}
	
	public void setPolicy(ElementParsingFailPolicy policy) {
		this.policy = Checks.isNotNull(policy, "policy");
	}
	
	/**
	 * Policy that determines how elements are handled when they can't be parsed successfully.
	 */
	public enum ElementParsingFailPolicy {
		/** Replaces input elements that fail to parse with {@code null}. */
		REPLACE_WITH_NULL,
		/** Abruptly stops parsing elements and marks the parsing as failed. */
		FAIL,
		/** Ignores input elements that fail to parse. */
		IGNORE
	}
	
}

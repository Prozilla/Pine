package dev.prozilla.pine.test;

import dev.prozilla.pine.common.system.Ansi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestLoggingExtension implements TestWatcher, BeforeTestExecutionCallback, BeforeAllCallback {
	
	private final Map<Class<?>, Integer> executedTests = new HashMap<>();
	private final Map<Class<?>, Integer> totalTests = new HashMap<>();
	private final Map<String, Boolean> lastMethodMap = new HashMap<>();
	
	@Override
	public void beforeAll(ExtensionContext context) {
		setTotalTestCount(context);
		System.out.println(Ansi.blue(context.getDisplayName()));
	}
	
	@Override
	public void beforeTestExecution(ExtensionContext context) {
		String prefix = isLastTest(context) ? "└── " : "├── ";
		System.out.println(Ansi.white(prefix) + Ansi.cyan(context.getDisplayName()));
	}
	
	@Override
	public void testSuccessful(ExtensionContext context) {
		String prefix = isLastTest(context) ? "    " : "│   ";
		System.out.println(Ansi.white(prefix + "└── ") + Ansi.green("✓ succeeded"));
	}
	
	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		String prefix = isLastTest(context) ? "    " : "│   ";
		System.out.println(Ansi.white(prefix + "└── ") + Ansi.red("× failed"));
		System.out.println(Ansi.white(prefix + "    └── ") + Ansi.red(cause.getMessage()));
	}
	
	/**
	 * Counts the total number of tests in the current test class.
	 */
	private void setTotalTestCount(ExtensionContext context) {
		Class<?> testClass = context.getTestClass().orElseThrow();
		int testCount = 0;
		
		for (Method method : testClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Test.class)) {
				testCount++;
			}
		}
		
		totalTests.put(testClass, testCount);
	}
	
	/**
	 * Checks if the current test is the last one in the class.
	 */
	protected boolean isLastTest(ExtensionContext context) {
		String testMethodKey = getTestMethodKey(context);
		
		if (lastMethodMap.containsKey(testMethodKey)) {
			return lastMethodMap.get(testMethodKey);
		}
		
		Class<?> testClass = context.getTestClass().orElseThrow();
		
		int totalTestCount = totalTests.getOrDefault(testClass, 0);
		int executedTestCount = executedTests.getOrDefault(testClass, 0);
		
		boolean isLast = (executedTestCount == totalTestCount - 1);
		lastMethodMap.put(testMethodKey, isLast);
		
		increaseExecutedTestCount(context);
		
		return isLast;
	}
	
	/**
	 * Increases the executed test count for the current test class.
	 */
	private void increaseExecutedTestCount(ExtensionContext context) {
		Class<?> testClass = context.getTestClass().orElseThrow();
		int executedTestCount = executedTests.getOrDefault(testClass, 0);
		executedTests.put(testClass, executedTestCount + 1);
	}
	
	/**
	 * Helper method to generate a unique key for each test method.
	 */
	private String getTestMethodKey(ExtensionContext context) {
		return context.getTestClass().orElseThrow().getName() + "#" + context.getTestMethod().orElseThrow().getName();
	}
	
}
package dev.prozilla.pine.test;

import dev.prozilla.pine.common.system.Ansi;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestPerformanceExtension extends TestLoggingExtension implements AfterTestExecutionCallback, AfterAllCallback {
	
	private long startTime;
	private long startMemory;
	
	private static final AtomicLong totalExecutionTime = new AtomicLong(0);
	private static final AtomicLong totalMemoryUsage = new AtomicLong(0);
	private static final AtomicInteger testCount = new AtomicInteger(0);
	
	@Override
	public void beforeTestExecution(ExtensionContext context) {
		super.beforeTestExecution(context);
		
		if (isPerformanceTest(context)) {
			System.gc();
			startTime = System.nanoTime();
			startMemory = getUsedMemory();
		}
	}
	
	@Override
	public void afterTestExecution(ExtensionContext context) {
		if (isPerformanceTest(context)) {
			long executionTime = (System.nanoTime() - startTime) / 1_000_000;
			long memoryUsed = (getUsedMemory() - startMemory) / 1024;
			
			totalExecutionTime.addAndGet(executionTime);
			totalMemoryUsage.addAndGet(memoryUsed);
			testCount.incrementAndGet();
			
			String prefix = isLastTest(context) ? "    " : "│   ";
			System.out.println(Ansi.white(prefix + "├── ") + "Execution time: " + executionTime + " ms");
			System.out.println(Ansi.white(prefix + "├── ") + "Memory used: " + memoryUsed + " KB");
		}
	}
	
	@Override
	public void afterAll(ExtensionContext extensionContext) {
		int finalTestCount = testCount.get();
		if (finalTestCount > 0) {
			long avgExecutionTime = totalExecutionTime.get() / finalTestCount;
			long avgMemoryUsage = totalMemoryUsage.get() / finalTestCount;
			
			System.out.println(Ansi.white("├── ") + ("Average execution time: " + avgExecutionTime + " ms"));
			System.out.println(Ansi.white("└── ") + ("Average memory used: " + avgMemoryUsage + " KB"));
		}
		
		totalExecutionTime.set(0);
		totalMemoryUsage.set(0);
		testCount.set(0);
	}
	
	@Override
	protected boolean isLastTest(ExtensionContext context) {
		return false;
	}
	
	private static boolean isPerformanceTest(ExtensionContext context) {
		return context.getTags().contains("performance");
	}
	
	private static long getUsedMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}
	
}

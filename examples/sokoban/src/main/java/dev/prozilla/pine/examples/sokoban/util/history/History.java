package dev.prozilla.pine.examples.sokoban.util.history;

import dev.prozilla.pine.examples.sokoban.util.Action;
import dev.prozilla.pine.examples.sokoban.util.Command;

import java.util.ArrayDeque;
import java.util.Deque;

public class History implements Action {

	private final Deque<Command> undoStack;
	private final Deque<Command> redoStack;
	
	public History() {
		undoStack = new ArrayDeque<>();
		redoStack = new ArrayDeque<>();
	}
	
	public void push(Command command) {
		undoStack.push(command);
		redoStack.clear();
	}
	
	@Override
	public void undo() {
		if (undoStack.isEmpty()) {
			return;
		}
		
		Command undoCommand = undoStack.pop();
		undoCommand.undo();
		redoStack.push(undoCommand);
	}
	
	@Override
	public void redo() {
		if (redoStack.isEmpty()) {
			return;
		}
		
		Command redoCommand = redoStack.pop();
		redoCommand.redo();
		undoStack.push(redoCommand);
	}
	
}

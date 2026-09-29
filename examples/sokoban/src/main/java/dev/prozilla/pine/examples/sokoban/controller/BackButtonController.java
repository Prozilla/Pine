package dev.prozilla.pine.examples.sokoban.controller;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.prefab.ui.Controller;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class BackButtonController implements Controller {
	
	@Override
	public void load(Node view) {
		Node backButton = view.getNodeById("back");
		backButton.onClick((event) -> GameManager.instance.loadMenu());
	}
	
}

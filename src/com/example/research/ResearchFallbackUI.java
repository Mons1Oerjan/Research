package com.example.research;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Fallback UI that gets called if the user chooses to not activate geolocation.
 */
public class ResearchFallbackUI extends UI {

	private static final long serialVersionUID = 1L;
	
	private static final String msg = "Geolocation not activated. Fallback UI activated.";
	
	@Override
	protected void init(VaadinRequest request){
		Label label = new Label(msg);
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(label);
		setContent(content);
	}
}

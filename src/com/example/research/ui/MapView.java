package com.example.research.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;


import com.example.research.backend.MapControl;
import com.example.research.backend.db.Data;
import com.vaadin.addon.touchkit.extensions.Geolocator;
import com.vaadin.addon.touchkit.extensions.PositionCallback;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * This class creates a world map and zooms in on the Dalhousie University campus.
 * The purpose of this class is to present / display data points to better visualize
 * the data for the user. 
 */
public class MapView extends NavigationView implements
        PositionCallback, LeafletClickListener {

	private static final long serialVersionUID = -5169285732263095853L;
	private MapControl controller;
	private LMap map;
    private Bounds extent;  
    private Button locatebutton; 
    private LMarker you = new LMarker(); 

   
    /**
     * main method of this view class that attaches this view to the UI, builds the view and updates markers.
     */
    @Override
    public void attach() {
        super.attach();
        buildView();
        updateMarkers();   
    };

    /**
     * Builds the mapView
     */
    private void buildView() {
    	setCaption("Map");
    	controller = new MapControl();
    	
        if (map == null) {
            map = new LMap();
            map.addMoveEndListener(new LeafletMoveEndListener() {

                @Override
                public void onMoveEnd(LeafletMoveEndEvent event) {
                    extent = event.getBounds();
                }
            });
            LTileLayer mapBoxTiles = new LTileLayer(
                    "http://{s}.tiles.mapbox.com/v3/vaadin.i1pikm9o/{z}/{x}/{y}.png");
            mapBoxTiles.setDetectRetina(true);
            map.addLayer(mapBoxTiles);
            map.setAttributionPrefix("Powered by <a href=\"leafletjs.com\">Leaflet</a> — &copy; "
            		+ "<a href='http://osm.org/copyright'>OpenStreetMap</a> contributors");
            map.setImmediate(true);
            map.setSizeFull();
            map.setZoomLevel(16);
            map.setId("lmap"); //test id
            
            setContent(map);
            
            // Default to Dal Uni
            you.setPoint(new Point(44.637393, -63.587189));
            setCenter();
        }

        // button to locate yourself on the map:
        locatebutton = new Button("Locate yourself", new ClickListener() {
			private static final long serialVersionUID = -5808755777465353812L;

			/**
			 * Detects geolocation when locatebutton is clicked
			 */
			@Override
            public void buttonClick(ClickEvent event) {
                Geolocator.detect(MapView.this);
                locatebutton.setCaption("Locating...");
            }
        });
        locatebutton.setDisableOnClick(true);
        setLeftComponent(locatebutton);
		
    }

    /**
     * Removes all markers.
     * Adds all markers.
     * Updates the new markers.
     */
    public void updateMarkers() {
        
    	List<Data> points = controller.getMapPoints((String)getSession().getAttribute("user"));
    	
    	Iterator<Component> iterator = map.iterator();
        Collection<Component> remove = new ArrayList<Component>();
        while (iterator.hasNext()) {
            Component next = iterator.next();
            if (next instanceof LMarker) {
                remove.add(next);
            }
        }
        for (Component component : remove) {
            map.removeComponent(component);
        }

        for(Data d : points) {    
        	LMarker leafletMarker = new LMarker();
            leafletMarker.setPoint(new Point(d.getLatitude(), d.getLongitude()));
            leafletMarker.addClickListener(this);

            map.addComponent(leafletMarker);
        }
    }

    /**
     * This method gets called if the user accepts to use GeoLocation.
     * It adds your location to the map with a pin. 
     */
    @Override
    public void onSuccess(Position position) {
    	
    	//marks your location on the map
        you.setPoint(new Point(position.getLatitude(), position.getLongitude()));
        if (you.getParent() == null) {
            map.addComponent(you);
        }
        
        /*
        // sets the current position in the UI
        ResearchUI ui = ResearchUI.getApp();
        ui.setCurrentLatitude(position.getLatitude());
        ui.setCurrentLongitude(position.getLongitude());
		*/
        
        //sets the center to the location you are at.
        setCenter();

        //resets the locatebutton:
        locatebutton.setCaption("Locate yourself");
        locatebutton.setEnabled(true);

    }

    /**
     * Sets the center of the map as long as the map exists.
     */
    private void setCenter() {
        if (map != null) {
            extent = new Bounds(you.getPoint());
            map.zoomToExtent(extent);
        }
    }

    /**
     * This method gets called if the user declines to use GeoLocation.
     * Shows a notification saying Geolocation request failed.
     */
    @Override
    public void onFailure(int errorCode) {
        Notification.show("Geolocation request failed. You must grant access for geolocation requests.",
                        Type.ERROR_MESSAGE);
    }
    
    /**
     * This method creates a popup window when a pin on the map is clicked.
     * This method is yet to be implemented.
     * @param Data d
     */
    public void showPopup(Data d){
    	/*
    	DataDetailPopover popover = new DataDetailPopover(d);
    	popover.showRelativeTo(getNavigationBar());
    	*/
    }

    /**
     * Shows the data location on the map and zooms in on the target location.
     * @param Data d
     */
    public void showData(Data d) {
        map.setCenter(d.getLatitude(), d.getLongitude());
        map.setZoomLevel(16);
    }

    /**
     * Overrides the button onClick method.
     * This method detects when a pin is being clicked and calls the showPopup method.
     */
    @Override
    public void onClick(LeafletClickEvent event) {
        Object o = event.getSource();
        if (o instanceof AbstractComponent) {
            Data data = (Data) ((AbstractComponent) o).getData();
            showPopup(data);
        }
    }

    
}

package com.divillafajar.app.pos.pos_app_sini.model.area.unit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateUnitAreaRequestModel {

	// --- Basic fields ---
	private String path;
	private String name;
	private String description;

	// --- Accommodation only fields ---
	private String tfloor;      // total floor
	private String troom;       // total bedroom
	private String tliving;     // total living room
	private String tbath;       // total bathroom
	private String tArea;       // total area

	// --- Amenities: feature facilities ---
	private List<String> featureFacilities;
	private String otherFeature;

	// --- Amenities: standard/general facilities ---
	private List<String> generalFacilities;
	private String otherStandard;

	// --- Amenities: on demand facilities ---
	private List<String> onDemandFacilities;
	private String otherOnDemand;

	// --- Add-on service ---
	private List<String> addOnService;
	private String otherAddOn;
}

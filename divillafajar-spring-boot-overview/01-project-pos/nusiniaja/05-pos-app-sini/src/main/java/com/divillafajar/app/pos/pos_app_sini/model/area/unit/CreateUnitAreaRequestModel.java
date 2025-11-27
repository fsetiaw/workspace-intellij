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
	private int tUnit;      // total Unit
	private int tfloor;      // total floor
	private int troom;       // total bedroom
	private int tliving;     // total living room
	private int tbath;       // total bathroom
	private int tArea;       // total area

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

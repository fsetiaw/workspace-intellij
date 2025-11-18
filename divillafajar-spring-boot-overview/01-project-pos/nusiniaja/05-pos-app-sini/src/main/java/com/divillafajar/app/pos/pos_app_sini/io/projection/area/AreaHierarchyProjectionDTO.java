package com.divillafajar.app.pos.pos_app_sini.io.projection.area;

public interface AreaHierarchyProjectionDTO {
    Long getId();
    String getName();
    Long getTopParentId();
    String getTopParentName();
    Long getLevel();
}
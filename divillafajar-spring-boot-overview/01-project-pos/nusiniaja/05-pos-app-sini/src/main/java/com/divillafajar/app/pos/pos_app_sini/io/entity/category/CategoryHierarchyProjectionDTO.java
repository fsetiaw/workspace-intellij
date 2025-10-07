package com.divillafajar.app.pos.pos_app_sini.io.entity.category;

public interface CategoryHierarchyProjectionDTO {
    Long getId();
    String getName();
    Long getTopParentId();
    String getTopParentName();
    Long getLevel();
}
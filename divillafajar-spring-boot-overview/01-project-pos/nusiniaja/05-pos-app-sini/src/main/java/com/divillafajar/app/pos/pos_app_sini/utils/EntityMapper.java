package com.divillafajar.app.pos.pos_app_sini.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {
    /**
     * Mapping list dari Entity ke DTO
     *
     * @param sourceList daftar entity
     * @param targetClass class DTO
     * @return daftar DTO
     */
    public static <S, T> List<T> mapList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> {
            try {
                T target = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, target);
                return target;
            } catch (Exception e) {
                throw new RuntimeException("Error mapping entity to DTO", e);
            }
        }).collect(Collectors.toList());
    }
}

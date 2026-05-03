package com.ricmen.notifications.mapper;

import org.mapstruct.Mapper;

import com.ricmen.notifications.domain.enums.Category;
import com.ricmen.notifications.dto.response.CategoryResponseDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  default CategoryResponseDto toDto(Category category) {
    return new CategoryResponseDto(
        category.name(),
        category.name().charAt(0) + category.name().substring(1).toLowerCase());
  }
}

package com.codewithmosh.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    void updateEntity(ProductDto productDto, @MappingTarget Product product);
}
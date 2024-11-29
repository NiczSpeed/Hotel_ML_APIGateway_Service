package com.ml.hotel_ml_apigateway_service.mapper;

import com.ml.hotel_ml_apigateway_service.dto.DeprecatedTokenDto;
import com.ml.hotel_ml_apigateway_service.model.DeprecatedToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeprecatedTokenMapper {
    DeprecatedTokenMapper Instance = Mappers.getMapper(DeprecatedTokenMapper.class);

    DeprecatedToken mapDeprecatedTokenToDeprecatedTokenDto(DeprecatedTokenDto deprecatedTokenDto);
}

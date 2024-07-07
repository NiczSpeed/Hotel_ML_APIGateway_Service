package com.ml.hotel_ml_apigateway_service.mapper;

import com.ml.hotel_ml_apigateway_service.dto.UserTokenDto;
import com.ml.hotel_ml_apigateway_service.model.UserToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserTokenMapper {

    UserTokenMapper Instance = Mappers.getMapper(UserTokenMapper.class);

    UserTokenDto mapUserTokenToUserTokenDto(UserToken userToken);
    UserToken mapUserTokenDtoToUserToken(UserTokenDto userTokenDto);

}

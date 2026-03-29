package com.zapter.zapter_backend.product.mapper;

import com.zapter.zapter_backend.product.domain.Model;
import com.zapter.zapter_backend.product.dto.model.ModelResponse;
import com.zapter.zapter_backend.product.dto.model.NewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelMapper {
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    Model toModel(NewModel newModel);

//    List<ModelResponse> toListOfModelResponse(List<Model> model);

//    ModelResponse toModelResponse(Model model);
}

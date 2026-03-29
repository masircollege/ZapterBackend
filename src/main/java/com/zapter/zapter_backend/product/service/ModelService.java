package com.zapter.zapter_backend.product.service;

import com.zapter.zapter_backend.product.domain.Brand;
import com.zapter.zapter_backend.product.domain.Model;
import com.zapter.zapter_backend.product.dto.model.ModelResponse;
import com.zapter.zapter_backend.product.dto.model.NewModel;
import com.zapter.zapter_backend.product.mapper.ModelMapper;
import com.zapter.zapter_backend.product.repository.BrandRepository;
import com.zapter.zapter_backend.product.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService {
    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;
    private final BrandRepository brandRepository;

    public ModelService(
            ModelRepository modelRepository,
            ModelMapper modelMapper,
            BrandRepository brandRepository
    ){
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
        this.brandRepository = brandRepository;
    }

    public void create(NewModel newModel){
        Brand brand = brandRepository.findById(newModel.brandId()).orElseThrow();
        Model model = new Model();
        model.setBrandModel(brand);
        model.setName(newModel.name());
        modelRepository.save(model);
    }

    public void update(Model model){
        modelRepository.save(model);
    }

    public void delete(Long id){
        try {
            modelRepository.deleteById(id);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public List<ModelResponse> get(){
        try {
            List<Model> models = modelRepository.findAll();
            return models.stream()
                    .map(model -> new ModelResponse(
                            model.getId(),
                            model.getName(),
                            model.getBrandModel().getId()
                    )).toList();
//            return modelMapper.toListOfModelResponse(modelRepository.findAll());
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
    public ModelResponse getById(Long id){
        try {
            Model model = modelRepository.findById(id).orElseThrow();
            return new ModelResponse(model.getId(), model.getName(), model.getBrandModel().getId());
//            return modelMapper.toModelResponse(modelRepository.findById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}

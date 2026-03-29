package com.zapter.zapter_backend.product.controller;

import com.zapter.zapter_backend.product.dto.model.ModelResponse;
import com.zapter.zapter_backend.product.dto.model.NewModel;
import com.zapter.zapter_backend.product.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zapter/admin/model")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService){
        this.modelService = modelService;
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<?> createModel(@Valid @RequestBody NewModel newModel){
        try {
            modelService.create(newModel);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<ModelResponse>> getModels(){
        try {
            return new ResponseEntity<>(modelService.get(),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModelById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(modelService.getById(id),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModelById(@PathVariable Long id){
        try {
            modelService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

}

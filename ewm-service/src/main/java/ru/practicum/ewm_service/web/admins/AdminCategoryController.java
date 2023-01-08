package ru.practicum.ewm_service.web.admins;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.mapper.CategoryMapper;
import ru.practicum.ewm_service.entity.model.category.CategoryDto;
import ru.practicum.ewm_service.service.admin.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Tag(name = "Контроллер категорий (Админ)", description = "Управление категориями администратором")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных"),
                @ApiResponse(responseCode = "404", description = "Не найденный объект"),
                @ApiResponse(responseCode = "403", description = "Доступ к ресурсу рграничен"),
                @ApiResponse(responseCode = "409", description = "Не уникальное поле в базе данных"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;
    private final CategoryMapper categoriesMapper;

    @Autowired
    public AdminCategoryController(AdminCategoryService adminCategoryService, CategoryMapper categoriesMapper) {
        this.adminCategoryService = adminCategoryService;
        this.categoriesMapper = categoriesMapper;
    }

    @Operation(
            summary = "Создание новой категоррии",
            description = "Создает новую категорию и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданную категорию",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
                    )
            })
    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(
            @Parameter(description = "Dto категории") @Valid @RequestBody final CategoryDto dto
    ) {
        Category category = adminCategoryService.createCategory(categoriesMapper.toCategory(dto));
        return new ResponseEntity<>(categoriesMapper.toCategoryDto(category), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление полей существующей категории",
            description = "Обновляет только поданные в RequestBody поля у сущности в базе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Обновляет категорию и возвращает ее",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
                    )
            })
    @PatchMapping()
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "Dto категории") @Valid @RequestBody final CategoryDto dto
    ) {
        Category category = adminCategoryService.updateCategory(categoriesMapper.toCategory(dto));
        return new ResponseEntity<>(categoriesMapper.toCategoryDto(category), HttpStatus.OK);
    }

    @Operation(summary = "Удаление категории по ее id")
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет категорию и ничего не возвращает"
    )
    @DeleteMapping("{categoryId}")
    public ResponseEntity deleteCategory(
            @Parameter(description = "Id категории") @PathVariable("categoryId") final long id
    ) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}


package ru.practicum.ewm_service.web.publics;

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
import ru.practicum.ewm_service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Контроллер категорий (Публичный)", description = "Поиск категорий неавторизованными пользователями")
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
public class PublicCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public PublicCategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Operation(
            summary = "Поиск определенной категории",
            description = "Ищет категорию по ее id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденую категорию",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
                    )
            })
    @GetMapping("/{compId}")
    public ResponseEntity<CategoryDto> getCategory(
            @Parameter(description = "Id кактегории") @PathVariable(value = "compId") final long compId
    ) {
        Category category = categoryService.getCategoryById(compId);
        return new ResponseEntity<>(categoryMapper.toCategoryDto(category), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск всех категорий",
            description = "Ищет и возвращает списиок событий согласно параметрам "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
                    )
            })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Количество объектов") @RequestParam(defaultValue = "10") final int size
    ) {
        List<Category> categories = categoryService.getCategories(from, size);
        return new ResponseEntity<>(categoryMapper.toCategoryDtos(categories), HttpStatus.OK);
    }
}

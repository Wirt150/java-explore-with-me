package ru.practicum.ewm_service.web.publics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.mapper.CategoryMapper;
import ru.practicum.ewm_service.entity.model.category.CategoryDto;
import ru.practicum.ewm_service.service.CategoryService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCategoryController.class)
public class PublicCategoryControllerTest {
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private CategoryMapper categoryMapper;
    @Autowired
    private MockMvc mvc;
    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("test")
                .build();
        categoryDto = CategoryDto.builder()
                .id(1L)
                .name("test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                categoryService,
                categoryMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт getCategory контроллера PublicCategoryControllerTest.")
    void getCategoryTest() throws Exception {
        when(categoryService.getCategoryById(anyLong())).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        //test
        mvc.perform(get("/categories/1")
                        .param("compId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(categoryService, times(1)).getCategoryById(anyLong());
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт getCategories контроллера PublicCategoryControllerTest.")
    void getCategoriesTest() throws Exception {
        when(categoryService.getCategories(anyInt(), anyInt())).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDtos(anyList())).thenReturn(List.of(categoryDto));

        //test
        mvc.perform(get("/categories/")
                        .param("from", "1")
                        .param("size", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(categoryDto.getName())));

        verify(categoryService, times(1)).getCategories(anyInt(), anyInt());
        verify(categoryMapper, times(1)).toCategoryDtos(anyList());
    }
}

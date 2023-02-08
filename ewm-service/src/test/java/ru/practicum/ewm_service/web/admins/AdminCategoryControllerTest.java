package ru.practicum.ewm_service.web.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.ewm_service.service.admin.AdminCategoryService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminCategoryController.class)
public class AdminCategoryControllerTest {
    @MockBean
    private AdminCategoryService adminCategoryService;
    @MockBean
    private CategoryMapper categoriesMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
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
                adminCategoryService,
                categoriesMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт createCategory контроллера AdminCategoryController.")
    void createCategoryTest() throws Exception {
        when(categoriesMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(adminCategoryService.createCategory(any(Category.class))).thenReturn(category);
        when(categoriesMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        //test
        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(categoriesMapper, times(1)).toCategory(any(CategoryDto.class));
        verify(adminCategoryService, times(1)).createCategory(any(Category.class));
        verify(categoriesMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт updateCategory контроллера AdminCategoryController.")
    void updateCategoryTest() throws Exception {
        when(categoriesMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(adminCategoryService.updateCategory(any(Category.class))).thenReturn(category);
        when(categoriesMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        //test
        mvc.perform(patch("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(categoriesMapper, times(1)).toCategory(any(CategoryDto.class));
        verify(adminCategoryService, times(1)).updateCategory(any(Category.class));
        verify(categoriesMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт deleteCategory контроллера AdminCategoryController.")
    void deleteCategoryTest() throws Exception {
        //test
        mvc.perform(delete("/admin/categories/1")
                        .param("categoryId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCategoryService, times(1)).deleteCategory(anyLong());
    }
}

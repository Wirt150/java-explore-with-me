package ru.practicum.ewm_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.repository.CategoryRepository;
import ru.practicum.ewm_service.repository.EventRepository;
import ru.practicum.ewm_service.service.admin.AdminCategoryService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceTest {
    @Autowired
    private AdminCategoryService categoryService;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private EventRepository eventRepository;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                categoryRepository,
                eventRepository
        );
    }

    @Test
    @DisplayName("Проверяем метод createCategory сервиса CategoryService.")
    void createCategoryMethodTest() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        //test
        Category test = categoryService.createCategory(category);
        assertEquals(1L, test.getId());
        assertEquals("test", test.getName());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Проверяем метод getCategoryById сервиса CategoryService.")
    void getCategoryByIdMethodTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        //test
        Category test = categoryService.getCategoryById(1L);
        assertEquals(1L, test.getId());
        assertEquals("test", test.getName());

        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод getCategories сервиса CategoryService.")
    void getCategoriesMethodTest() {
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(category)));

        //test
        List<Category> test = categoryService.getCategories(0, 10);
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getName());

        verify(categoryRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод updateCategory сервиса CategoryService.")
    void updateCategoryMethodTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        final Category update = Category.builder().name("testtest").id(1L).build();

        //test
        Category test = categoryService.updateCategory(update);
        assertEquals(1L, test.getId());
        assertEquals("testtest", test.getName());

        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Проверяем метод deleteCategory сервиса CategoryService.")
    void deleteCategoryMethodTest() {
        when(eventRepository.existsByCategoryId(anyLong())).thenReturn(false);

        categoryService.deleteCategory(1L);

        //test
        verify(eventRepository, times(1)).existsByCategoryId(anyLong());
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }
}

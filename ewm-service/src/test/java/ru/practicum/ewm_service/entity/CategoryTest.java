package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm_service.entity.model.category.CategoryDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CategoryTest {

    @Autowired
    private JacksonTester<Category> json;
    @Autowired
    private JacksonTester<CategoryDto> jsonDto;

    @Test
    @DisplayName("Проверяем правильность сериализации Category.")
    void whenCreateCategoryAndSerializableHim() throws Exception {

        final Category category = Category.builder()
                .id(1L)
                .name("test")
                .build();

        JsonContent<Category> result = json.write(category);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации CategoryDto.")
    void whenCreateCategoryDtoAndSerializableHim() throws Exception {

        final CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name("test")
                .build();

        JsonContent<CategoryDto> result = jsonDto.write(categoryDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }
}

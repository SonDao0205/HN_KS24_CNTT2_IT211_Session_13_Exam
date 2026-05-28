package ra.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ra.demo.controller.BookController;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.service.BookService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void getBooks_statusOK() throws Exception {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookService.getBooks()).thenReturn(books);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void getBookById_found() throws Exception {
        Book book = Book.builder().id(1L).title("Test Book").build();
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void getBookById_notFound() throws Exception {
        when(bookService.getBookById(1L)).thenThrow(new BookNotFound("Not Found"));

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isNotFound());
    }
}

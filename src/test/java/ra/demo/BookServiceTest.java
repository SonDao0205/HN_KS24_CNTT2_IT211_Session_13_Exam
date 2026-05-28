package ra.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookResporitory bookResporitory;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_returnList() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookResporitory.findAll()).thenReturn(books);

        List<Book> result = bookService.getBooks();
        assertEquals(2, result.size());
    }

    @Test
    void getBookById_found() {
        Book book = Book.builder().id(1L).title("Test Book").build();
        when(bookResporitory.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void getBookById_notFound() {
        when(bookResporitory.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFound.class, () -> bookService.getBookById(1L));
    }
}
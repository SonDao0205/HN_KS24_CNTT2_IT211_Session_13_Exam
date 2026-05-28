package ra.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ra.demo.exception.BookNotFound;
import ra.demo.model.dto.request.BookDTO;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookResporitory bookResporitory;

    @Override
    public List<Book> getBooks() {
        log.info("Thực hiện lấy danh sách tất cả sách");
        return bookResporitory.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        log.debug("Nhận request lấy thông tin sách với ID: {}", id);
        return bookResporitory.findById(id).orElseThrow(()-> {
            log.error("Lỗi: Không tìm thấy sách với mã {}", id);
            return new BookNotFound("Không tồn tại sách có mã " + id);
        });
    }

    @Override
    public Book insertBook(BookDTO bookDTO) {
        log.debug("Nhận dữ liệu thêm mới sách: {}", bookDTO);
        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();
        Book savedBook = bookResporitory.save(book);
        log.info("Thêm mới sách thành công với ID: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) {
        log.debug("Nhận dữ liệu cập nhật sách: {}, id cập nhật : {}", bookDTO,id);
        bookResporitory.findById(id).orElseThrow(()-> new BookNotFound("Không tồn tại sách có mã "+id));
        Book book = Book.builder()
                .id(id)
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();
        Book savedBook = bookResporitory.save(book);
        log.info("Cập nhật thông tin sách thành công với ID: {}", id);
        return savedBook;
    }

    @Override
    public boolean deleteBook(Long id) {
        log.debug("Nhận dữ liệu xoá sách id : {}", id);
        bookResporitory.findById(id).orElseThrow(()-> new BookNotFound("Không tồn tại sách có mã "+id));
        bookResporitory.deleteById(id);
        log.info("Xoá sách thành công với id : {}", id);
        return true;
    }

    @Override
    public Book updatePartialBook(Long id, BookDTO bookDTO) {
        log.debug("Nhận dữ liệu cập nhật 1 phần sách : {}, id cập nhật : {}", bookDTO,id);
        Book book = bookResporitory.findById(id).orElseThrow(() -> new BookNotFound("Không tồn tại sách có mã " + id));
        if(!bookDTO.getTitle().isBlank()){
            book.setTitle(bookDTO.getTitle());
        }
        if(!bookDTO.getAuthor().isBlank()){
            book.setAuthor(book.getAuthor());
        }
        if(!bookDTO.getCategory().isBlank()){
            book.setCategory(bookDTO.getCategory());
        }
        if(bookDTO.getQuantity()!=null && bookDTO.getQuantity()>=0){
            book.setQuantity(bookDTO.getQuantity());
        }
        Book savedBook = bookResporitory.save(book);
        log.info("Cập nhật 1 phần thông tin sách thành công với id : {}", id);
        return savedBook;
    }
}

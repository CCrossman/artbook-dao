package com.artbook.dao.unit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.artbook.dao.api.BookController;
import com.artbook.dao.web.entity.Author;
import com.artbook.dao.web.entity.Book;
import com.artbook.dao.web.entity.User;
import com.artbook.dao.web.service.BookService;

/**
 * <b>Book Controller Unit Test</b><br>
 * I suggest to use Mockito
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@RunWith(SpringRunner.class)
public class BookControllerTest {

	private MockMvc mockMvc;
	private @Mock BookService bookService;
	private @InjectMocks BookController bookController = new BookController();

	private Long bookId = 1L;
	private String bookName = "Spring Boot Quick Start";
	private User user = new User();

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {
		user.setUsername("Jackie");
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.bookController).build();
	}

	@Test
	public void testCreateBook() throws Exception {
		Book savedBook = new Book();
		savedBook.setId(bookId);
		savedBook.setBookName(bookName);
		savedBook.setCreateBy(user);

		Book newBook = new Book();
		newBook.setBookName(bookName);

		Mockito.when(bookService.createBook(bookName)).thenReturn(savedBook);

		this.mockMvc.perform(
				post("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(newBook)))
				.andExpect(status().isCreated());

	}

	@Test
	public void testGetBook() throws Exception {
		Book savedBook = new Book();
		savedBook.setId(bookId);
		savedBook.setBookName(bookName);
		savedBook.setCreateBy(user);

		Mockito.when(bookService.getBook(bookId)).thenReturn(savedBook);

		this.mockMvc.perform(get("/api/books/" + bookId)).andExpect(status().isOk());

	}

	@Test
	public void testDeleteBook() throws Exception {
		Mockito.doNothing().when(bookService).deleteBook(bookId);
		this.mockMvc.perform(delete("/api/books/" + bookId)).andExpect(status().isNoContent());
	}

	@Test
	public void testAssignAuthor() throws Exception {

		Author author = new Author();
		author.setId(1L);

		Book savedBook = new Book();
		savedBook.setId(bookId);
		savedBook.setBookName(bookName);
		savedBook.setCreateBy(user);

		Mockito.when(bookService.assignAuthor(bookId, 1L)).thenReturn(savedBook);

		this.mockMvc.perform(put("/api/books/" + bookId + "/authors").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(author))).andExpect(status().isOk());

	}
}

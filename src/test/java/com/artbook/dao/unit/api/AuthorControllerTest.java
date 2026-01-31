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
import com.artbook.dao.api.AuthorController;
import com.artbook.dao.web.entity.Author;
import com.artbook.dao.web.entity.Book;
import com.artbook.dao.web.entity.User;
import com.artbook.dao.web.service.AuthorService;

/**
 * <b>Author Controller Unit Test</b><br>
 * I suggest to use Mockito
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@RunWith(SpringRunner.class)
public class AuthorControllerTest {

	private MockMvc mockMvc;
	private @Mock AuthorService authorService;
	private @InjectMocks AuthorController authorController = new AuthorController();

	private Long authorId = 1L;
	private String authorName = "Jackie";
	private User user = new User();

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {
		user.setUsername(authorName);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.authorController).build();
	}

	@Test
	public void testCreateAuthor() throws Exception {
		Author savedAuthor = new Author();
		savedAuthor.setId(authorId);
		savedAuthor.setAuthorName(authorName);
		savedAuthor.setCreateBy(user);

		Author newAuthor = new Author();
		newAuthor.setAuthorName(authorName);

		Mockito.when(authorService.createAuthor(authorName)).thenReturn(savedAuthor);

		this.mockMvc.perform(post("/api/authors").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(newAuthor))).andExpect(status().isCreated());

	}

	@Test
	public void testGetAuthor() throws Exception {
		Author savedAuthor = new Author();
		savedAuthor.setId(authorId);
		savedAuthor.setAuthorName(authorName);
		savedAuthor.setCreateBy(user);

		Mockito.when(authorService.getAuthor(authorId)).thenReturn(savedAuthor);

		this.mockMvc.perform(get("/api/authors/" + authorId)).andExpect(status().isOk());

	}

	@Test
	public void testDeleteAuthor() throws Exception {
		Mockito.doNothing().when(authorService).deleteAuthor(authorId);
		this.mockMvc.perform(delete("/api/authors/" + authorId)).andExpect(status().isNoContent());
	}

	@Test
	public void testAssignBook() throws Exception {

		Book book = new Book();
		book.setId(1L);

		Author savedAuthor = new Author();
		savedAuthor.setId(authorId);
		savedAuthor.setAuthorName(authorName);
		savedAuthor.setCreateBy(user);

		Mockito.when(authorService.assignBook(authorId, 1L)).thenReturn(savedAuthor);

		this.mockMvc.perform(put("/api/authors/" + authorId + "/books").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(book))).andExpect(status().isOk());

	}
}

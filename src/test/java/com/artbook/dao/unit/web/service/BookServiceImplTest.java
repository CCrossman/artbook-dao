package com.artbook.dao.unit.web.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.artbook.dao.support.exception.DuplicateException;
import com.artbook.dao.support.exception.NotFoundException;
import com.artbook.dao.web.entity.Author;
import com.artbook.dao.web.entity.Book;
import com.artbook.dao.web.entity.BookAuthor;
import com.artbook.dao.web.repository.AuthorRepository;
import com.artbook.dao.web.repository.BookAuthorRepository;
import com.artbook.dao.web.repository.BookRepository;
import com.artbook.dao.web.service.BookService;
import com.artbook.dao.web.service.impl.BookServiceImpl;

/**
 * <b>Book Service Unit Test</b><br>
 * I suggest to use Mockito
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@RunWith(SpringRunner.class)
public class BookServiceImplTest {

	private @Mock BookRepository bookRepository;
	private @Mock AuthorRepository authorRepository;
	private @Mock BookAuthorRepository bookAuthorRepository;
	private @InjectMocks BookService bookService = new BookServiceImpl();

	private Long authorId = 1L;
	private Long bookId = 1L;
	private String authorName = "Jackie";
	private String bookName = "Spring Boot Quick Start";

	@Test
	public void testGetBookById() {
		Book book = new Book();
		book.setId(bookId);
		book.setBookName(bookName);

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(book);

		Book retrievedBook = bookService.getBook(bookId);

		Assert.assertEquals(book, retrievedBook);
	}

	@Test(expected = NotFoundException.class)
	public void testGetNonExistBookById() {

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(null);

		bookService.getBook(bookId);

	}

	@Test
	public void testCreateBookByBookName() {

		Book book = new Book();
		book.setBookName(bookName);

		Mockito.when(bookRepository.findByBookName(bookName)).thenReturn(null);
		Mockito.when(bookRepository.save(book)).thenReturn(book);

		Book savedBook = bookService.createBook(bookName);

		Assert.assertEquals(book, savedBook);
	}

	@Test(expected = DuplicateException.class)
	public void testCreateDuplicateBookByBookName() {
		Book book = new Book();
		book.setBookName(bookName);

		Mockito.when(bookRepository.findByBookName(bookName)).thenReturn(book);
		Mockito.when(bookRepository.save(book)).thenReturn(book);

		bookService.createBook(bookName);
	}

	@Test
	public void testDeleteBookById() {
		Book book = new Book();
		book.setId(bookId);
		book.setBookName(bookName);

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(book);

		bookService.deleteBook(bookId);

		Mockito.verify(bookAuthorRepository, Mockito.times(1)).deleteByBook(book);
		Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
	}

	@Test(expected = NotFoundException.class)
	public void testDeleteNonExistBookById() {

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(null);

		bookService.deleteBook(bookId);
	}

	@Test
	public void testAssignAuthor() {
		Book book = new Book();
		book.setId(bookId);
		book.setBookName(bookName);

		Author author = new Author();
		author.setId(authorId);
		author.setAuthorName(authorName);

		BookAuthor bookAuthor = new BookAuthor();
		bookAuthor.setBook(book);
		bookAuthor.setAuthor(author);

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(book);
		Mockito.when(authorRepository.findOne(authorId)).thenReturn(author);
		Mockito.when(bookAuthorRepository.findByBookAndAuthor(book, author)).thenReturn(null);
		Mockito.when(bookAuthorRepository.save(bookAuthor)).thenReturn(bookAuthor);

		Book assignedBook = bookService.assignAuthor(bookId, authorId);

		Assert.assertEquals(book, assignedBook);
	}

	@Test(expected = NotFoundException.class)
	public void testAssignAuthorWithNonExistBook() {

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(null);

		bookService.assignAuthor(bookId, authorId);
	}

	@Test(expected = NotFoundException.class)
	public void testAssignAuthorWithNonExistAuthor() {

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(null);

		bookService.assignAuthor(bookId, authorId);
	}
}

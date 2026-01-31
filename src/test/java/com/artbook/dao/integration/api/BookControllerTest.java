package com.artbook.dao.integration.api;

import java.net.URI;
import java.net.URISyntaxException;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.artbook.dao.api.resource.BookResource;
import com.artbook.dao.integration.base.IntegrationTestBase;
import com.artbook.dao.integration.base.TestScopeAuthentication;
import com.artbook.dao.web.entity.Author;
import com.artbook.dao.web.entity.Book;

/**
 * Integration Test of Book API
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookControllerTest extends IntegrationTestBase {

	@Autowired
	private TestRestTemplate restTemplate;

	private Long existInMemoryAuthorId = 1L;
	private Long newBookId = 2L;
	private String newBookName = "Jackie's Knowledge share";

	@Test
	public void test_001_CreateBook() {
		Book book = new Book();
		book.setBookName(newBookName);

		ResponseEntity<BookResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.ADMIN_NAME, TestScopeAuthentication.ADMIN_PASSWORD)
				.postForEntity("/api/books/", book, BookResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getBody()).isInstanceOf(BookResource.class);
	}

	@Test
	public void test_002_GetBook() {
		ResponseEntity<BookResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.USER_NAME, TestScopeAuthentication.USER_PASSWORD)
				.getForEntity("/api/books/" + newBookId, BookResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isInstanceOf(BookResource.class);
	}

	@Test
	public void test_003_AssignAuthor() {
		Author author = new Author();
		author.setId(existInMemoryAuthorId);

		RequestEntity<Author> requestEntity = new RequestEntity<Author>(author, HttpMethod.PUT,
				URI.create("/api/books/" + newBookId + "/authors"));

		ResponseEntity<BookResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.ADMIN_NAME, TestScopeAuthentication.ADMIN_PASSWORD)
				.exchange(requestEntity, BookResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isInstanceOf(BookResource.class);
	}

	@Test
	public void test_004_DeleteAuthor() throws URISyntaxException {
		RequestEntity<Void> requestEntity = new RequestEntity<Void>(HttpMethod.DELETE,
				URI.create("/api/books/" + newBookId));

		ResponseEntity<BookResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.ADMIN_NAME, TestScopeAuthentication.ADMIN_PASSWORD)
				.exchange(requestEntity, BookResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThat(response.getBody()).isNull();
	}
}

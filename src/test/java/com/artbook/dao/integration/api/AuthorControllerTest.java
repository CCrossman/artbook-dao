package com.artbook.dao.integration.api;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.artbook.dao.api.resource.AuthorResource;
import com.artbook.dao.integration.base.IntegrationTestBase;
import com.artbook.dao.integration.base.TestScopeAuthentication;
import com.artbook.dao.web.entity.Author;
import com.artbook.dao.web.entity.Book;

import java.net.URI;
import java.net.URISyntaxException;

import org.assertj.core.api.Assertions;

/**
 * Integration Test of Author API
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorControllerTest extends IntegrationTestBase {

	@Autowired
	private TestRestTemplate restTemplate;

	private Long existInMemoryBookId = 1L;
	private Long newAuthorId = 2L;
	private String newAauthorName = "Wenbo";

	@Test
	public void test_001_CreateAuthor() {
		Author author = new Author();
		author.setAuthorName(newAauthorName);

		ResponseEntity<AuthorResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.ADMIN_NAME, TestScopeAuthentication.ADMIN_PASSWORD)
				.postForEntity("/api/authors/", author, AuthorResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getBody()).isInstanceOf(AuthorResource.class);
	}

	@Test
	public void test_002_GetAuthor() {
		ResponseEntity<AuthorResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.USER_NAME, TestScopeAuthentication.USER_PASSWORD)
				.getForEntity("/api/authors/" + newAuthorId, AuthorResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isInstanceOf(AuthorResource.class);
	}

	@Test
	public void test_003_AssignBook() {
		Book book = new Book();
		book.setId(existInMemoryBookId);

		RequestEntity<Book> requestEntity = new RequestEntity<Book>(book, HttpMethod.PUT,
				URI.create("/api/authors/" + newAuthorId + "/books"));

		ResponseEntity<AuthorResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.ADMIN_NAME, TestScopeAuthentication.ADMIN_PASSWORD)
				.exchange(requestEntity, AuthorResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isInstanceOf(AuthorResource.class);
	}

	@Test
	public void test_004_DeleteAuthor() throws URISyntaxException {
		RequestEntity<Void> requestEntity = new RequestEntity<Void>(HttpMethod.DELETE,
				URI.create("/api/authors/" + newAuthorId));

		ResponseEntity<AuthorResource> response = this.restTemplate
				.withBasicAuth(TestScopeAuthentication.ADMIN_NAME, TestScopeAuthentication.ADMIN_PASSWORD)
				.exchange(requestEntity, AuthorResource.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThat(response.getBody()).isNull();
	}
}

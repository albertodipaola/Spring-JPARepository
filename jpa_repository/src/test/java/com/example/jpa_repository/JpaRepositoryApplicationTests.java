package com.example.jpa_repository;

import java.util.List;

//import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

import com.example.jpa_repository.models.Author;
import com.example.jpa_repository.models.Comment;
import com.example.jpa_repository.models.Post;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaRepositoryApplicationTests {
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JpaAuthorRepository jpaAuthorRepository;
	@Autowired
	private CrudAuthorRepository crudAuthorRepository;

	@BeforeEach
	public void createData(){
		Author a = new Author();
		a.setFirstname("Alberto");
		a.setLastname("Di Paola");
		a.setEmail("test@blog.it");
		entityManager.persist(a);
		
		Post p = new Post();
		p.setTitle("nuovo iphone 14");
		p.setBody("bla bla bla");
		p.setPublishDate("12042023");
		p.setAuthor(a);
		entityManager.persist(p);

		Comment c1 = new Comment();
		c1.setEmail("mail@ciao.it");
		c1.setBody("fai kifo!!1!11!");
		c1.setDate("12042023");
		c1.setPost(p);
		entityManager.persist(c1);

		Comment c2 = new Comment();
		c2.setEmail("tizio@caio.it");
		c2.setBody("fai tanto kifo!!1!11!");
		c2.setDate("12042023");
		c2.setPost(p);
		entityManager.persist(c2);
	}

	@Test
	void checkAuthor() {
		List<Author> authors = entityManager
			.createQuery("SELECT a FROM Author a", Author.class)
			.getResultList();
	
		assertThat(authors).hasSize(1);
	}

	@Test
	void jpaauthorRepositoryCheck(){
		assertThat(jpaAuthorRepository.count()).isEqualTo(1);
		assertThat(jpaAuthorRepository.findAll()).first().extracting("firstname").isEqualTo("alberto");

		Author a = jpaAuthorRepository.findAll().get(0);
		assertThat(jpaAuthorRepository.findById(a.getId()).get())
			.isNotNull()
			.extracting("lastname")
			.isEqualTo("di paola");
	}

	@Test
	void findByLastname(){
		assertThat(jpaAuthorRepository.findByLastname("di paola"))
			.first()
			.extracting("lastname")
			.isEqualTo("di paola");
	}

	@Test
	void findByFirstnameAndLastname() {
		assertThat(jpaAuthorRepository.findByFirstnameAndLastname("alberto", "di paola"))
				.first()
				.extracting("firstname")
				.isEqualTo("alberto");
	}

	@Test
	void findByFirstnameNotIgnoreCase(){
		assertThat(jpaAuthorRepository.findByFirstnameNotIgnoreCase("Alberto")).hasSize(1);
		assertThat(jpaAuthorRepository.findByFirstnameNotIgnoreCase("alberto")).hasSize(0);
	}

	@Test
	void findByFirstnameEquals() {
		assertThat(jpaAuthorRepository.findByFirstnameEquals("Alberto")).hasSize(1);
	}

	@Test
	void findByFirstnameContains() {
		assertThat(jpaAuthorRepository.findByFirstnameContains("e")).hasSize(1);
	}

	@Test
	void crudRepository(){
		Author a = new Author();
		a.setFirstname("Giuseppe");
		a.setLastname("Sorcio");
		a.setEmail("test2@blog.it");

		crudAuthorRepository.save(a);
		assertThat(crudAuthorRepository.findAll()).hasSize(2);

	}

}

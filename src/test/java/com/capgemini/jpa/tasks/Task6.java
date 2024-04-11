package com.capgemini.jpa.tasks;

import com.capgemini.jpa.entities.Comment;
import com.capgemini.jpa.entities.Follower;
import com.capgemini.jpa.repositories.CommentRepository;
import com.capgemini.jpa.repositories.EventRepository;
import com.capgemini.jpa.repositories.FollowerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@DataJpaTest
class Task6 {
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EventRepository eventRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void shouldGetFollowerUsingNamedGraph() {
        commentRepository.saveAndFlush(
                new Comment(1L, "test comment 1",
                        eventRepository.findById(11L).orElse(null)
                )
        );
        commentRepository.saveAndFlush(
                new Comment(2L, "test comment 2",
                        eventRepository.findById(12L).orElse(null)
                )
        );
        commentRepository.saveAndFlush(
                new Comment(3L, "test comment 3",
                        eventRepository.findById(13L).orElse(null)
                )
        );
        assertThat(commentRepository.findAll().size(), is(3));

        followerRepository.saveAndFlush(new Follower(1L, "Follower1", LocalDateTime.now(), commentRepository.getById(2L)));
        followerRepository.saveAndFlush(new Follower(2L, "Follower2", LocalDateTime.now(), commentRepository.getById(3L)));
        assertThat("Followers should be added", followerRepository.findAll().size(), is(2));

        var followers = followerRepository.findAllByUserId("Follower2");
        assertThat(followers.size(), is(1));
    }
}
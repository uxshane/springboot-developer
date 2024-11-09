package me.minkyu.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.minkyu.springbootdeveloper.domain.Article;
import me.minkyu.springbootdeveloper.dto.AddArticleRequest;
import me.minkyu.springbootdeveloper.dto.UpdateArticleRequest;
import me.minkyu.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글을 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        result.andExpect(MockMvcResultMatchers.status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo("title");
        assertThat(articles.get(0).getContent()).isEqualTo("content");

    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다")
    @Test
    public void findAllArticles() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content")
                        .value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title")
                        .value("title"));

    }

    @DisplayName("findArticleById: 블로그 글 조회에 성공한다")
    @Test
    public void findArticleById() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        AddArticleRequest request = new AddArticleRequest(title, content);

        Article savedArticle = blogRepository.save(Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build());

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get(url, savedArticle.getId()));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title"));

    }

    @DisplayName("deleteArticle: 블로그 글을 삭제 성공한다")
    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        AddArticleRequest request = new AddArticleRequest(title, content);

        Article savedArticle = blogRepository.save(Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build());

        mockMvc.perform(MockMvcRequestBuilders.delete(url, savedArticle.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.isEmpty());
    }

    @DisplayName("updateArticle: 블로그 글 수정 성공한다")
    @Test
    public void updateArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "hehe";
        final String content = "hihi";

        Article savedArticle = blogRepository.save(
                Article.builder()
                        .title(title)
                        .content(content)
                        .build());

        final String newTitle = "new title";
        final String newContent = "new content";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put(url, savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo("new title");
        assertThat(article.getContent()).isEqualTo("new content");
    }

}











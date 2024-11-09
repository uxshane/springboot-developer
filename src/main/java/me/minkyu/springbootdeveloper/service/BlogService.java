package me.minkyu.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.minkyu.springbootdeveloper.domain.Article;
import me.minkyu.springbootdeveloper.dto.AddArticleRequest;
import me.minkyu.springbootdeveloper.dto.UpdateArticleRequest;
import me.minkyu.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Not Found: " + id));
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found: " + id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }

}

package com.wanted.socialintegratefreed.config.restdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * mockMvc를 사용할 때, 자동으로 rest docs 작성하게 한다.
 */
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractRestDocsTests {

  @Autowired
  protected RestDocumentationResultHandler restDocs;

  @Autowired
  protected MockMvc mockMvc;

  @BeforeEach
  void setUp(
      final WebApplicationContext context,
      final RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restDocs)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .apply(SecurityMockMvcConfigurers.springSecurity()) // 스프링 시큐리티 설정 적용
        .defaultRequest(post("/**").with(csrf()))
        .defaultRequest(patch("/**").with(csrf()))
        .defaultRequest(delete("/**").with(csrf()))
        .build();
  }
}
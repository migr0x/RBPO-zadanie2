package ru.mtusi.rbpo.lab2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RbpoLab2ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void allResourcesSupportCrudAndDeletingRelatedObjectsCleansReferences() throws Exception {
        JsonNode section = create("/api/editorial-sections",
                "{\"name\":\"Technology\",\"publicationAvailable\":true}");
        long sectionId = section.get("id").asLong();
        mockMvc.perform(get("/api/editorial-sections"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Technology"));
        mockMvc.perform(get("/api/editorial-sections/{id}", sectionId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(sectionId));
        mockMvc.perform(patch("/api/editorial-sections/{id}", sectionId)
                        .contentType("application/json")
                        .content("{\"publicationAvailable\":false}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.publicationAvailable").value(false));

        JsonNode user1 = create("/api/users",
                "{\"username\":\"editor1\",\"displayName\":\"First Editor\",\"email\":\"editor1@example.test\"}");
        JsonNode user2 = create("/api/users",
                "{\"username\":\"editor2\",\"displayName\":\"Second Editor\",\"email\":\"editor2@example.test\"}");
        long user1Id = user1.get("id").asLong();
        long user2Id = user2.get("id").asLong();
        mockMvc.perform(post("/api/articles")
                        .contentType("application/json")
                        .content("{\"sectionId\":" + sectionId + ",\"authorId\":" + user1Id + ","
                                + "\"title\":\"Unavailable\",\"text\":\"Text\",\"status\":\"PUBLISHED\"}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        mockMvc.perform(get("/api/users/{id}", user1Id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.username").value("editor1"));
        mockMvc.perform(patch("/api/users/{id}", user2Id)
                        .contentType("application/json")
                        .content("{\"displayName\":\"Second Editor Updated\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.displayName").value("Second Editor Updated"));

        JsonNode article1 = create("/api/articles", articleJson(sectionId, user1Id, "First news"));
        JsonNode article2 = create("/api/articles", articleJson(sectionId, user2Id, "Second news"));
        long article1Id = article1.get("id").asLong();
        long article2Id = article2.get("id").asLong();
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        mockMvc.perform(get("/api/articles/{id}", article1Id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("First news"));
        mockMvc.perform(patch("/api/articles/{id}", article2Id)
                        .contentType("application/json")
                        .content("{\"status\":\"IN_REVIEW\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("IN_REVIEW"));

        Instant beforeReview = Instant.now();
        JsonNode review1 = create("/api/editorial-reviews",
                "{\"articleId\":" + article1Id + ",\"decision\":\"NEEDS_REVISION\","
                        + "\"remarks\":\"Add a source\",\"reviewedAt\":\"2000-01-01T00:00:00Z\"}");
        JsonNode review2 = create("/api/editorial-reviews",
                "{\"articleId\":" + article2Id + ",\"decision\":\"APPROVED\",\"remarks\":\"Ready\"}");
        long review1Id = review1.get("id").asLong();
        long review2Id = review2.get("id").asLong();
        Instant actualReviewTime = Instant.parse(review1.get("reviewedAt").asText());
        assertThat(actualReviewTime).isAfter(beforeReview.minusSeconds(2));
        assertThat(actualReviewTime).isAfter(Instant.parse("2020-01-01T00:00:00Z"));
        mockMvc.perform(get("/api/editorial-reviews"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        mockMvc.perform(get("/api/editorial-reviews/{id}", review1Id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.articleId").value(article1Id));
        mockMvc.perform(patch("/api/editorial-reviews/{id}", review1Id)
                        .contentType("application/json")
                        .content("{\"remarks\":\"Source added\",\"decision\":\"APPROVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remarks").value("Source added"))
                .andExpect(jsonPath("$.decision").value("APPROVED"));

        JsonNode issue = create("/api/edition-issues",
                "{\"date\":\"2026-10-31\",\"articleIds\":[" + article1Id + "," + article2Id + "],\"status\":\"DRAFT\"}");
        long issueId = issue.get("id").asLong();
        mockMvc.perform(get("/api/edition-issues"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mockMvc.perform(get("/api/edition-issues/{id}", issueId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.articleIds.length()").value(2));
        mockMvc.perform(patch("/api/edition-issues/{id}", issueId)
                        .contentType("application/json")
                        .content("{\"status\":\"PUBLISHED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PUBLISHED"));

        mockMvc.perform(delete("/api/users/{id}", user1Id)).andExpect(status().isNoContent()).andExpect(content().string(""));
        mockMvc.perform(get("/api/articles/{id}", article1Id)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/editorial-reviews/{id}", review1Id)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/edition-issues/{id}", issueId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.articleIds.length()").value(1))
                .andExpect(jsonPath("$.articleIds[0]").value(article2Id));

        mockMvc.perform(delete("/api/editorial-reviews/{id}", review2Id))
                .andExpect(status().isNoContent()).andExpect(content().string(""));
        mockMvc.perform(delete("/api/articles/{id}", article2Id))
                .andExpect(status().isNoContent()).andExpect(content().string(""));
        mockMvc.perform(get("/api/edition-issues/{id}", issueId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.articleIds.length()").value(0));

        JsonNode secondSection = create("/api/editorial-sections",
                "{\"name\":\"Science\",\"publicationAvailable\":true}");
        long secondSectionId = secondSection.get("id").asLong();
        JsonNode sectionArticle = create("/api/articles", articleJson(secondSectionId, user2Id, "Science news"));
        long sectionArticleId = sectionArticle.get("id").asLong();
        JsonNode sectionReview = create("/api/editorial-reviews",
                "{\"articleId\":" + sectionArticleId + ",\"decision\":\"APPROVED\",\"remarks\":\"OK\"}");
        long sectionReviewId = sectionReview.get("id").asLong();
        JsonNode secondIssue = create("/api/edition-issues",
                "{\"date\":\"2026-11-01\",\"articleIds\":[" + sectionArticleId + "],\"status\":\"DRAFT\"}");
        long secondIssueId = secondIssue.get("id").asLong();
        mockMvc.perform(delete("/api/editorial-sections/{id}", secondSectionId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/articles/{id}", sectionArticleId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/editorial-reviews/{id}", sectionReviewId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/edition-issues/{id}", secondIssueId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.articleIds.length()").value(0));

        mockMvc.perform(delete("/api/users/{id}", user2Id)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/editorial-sections/{id}", sectionId)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/edition-issues/{id}", issueId)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/edition-issues/{id}", secondIssueId)).andExpect(status().isNoContent());
    }

    @Test
    void rejectsReferencesToMissingResources() throws Exception {
        mockMvc.perform(post("/api/articles")
                        .contentType("application/json")
                        .content(articleJson(9999, 9999, "Orphan article")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/edition-issues")
                        .contentType("application/json")
                        .content("{\"date\":\"2026-10-31\",\"articleIds\":[9999],\"status\":\"DRAFT\"}"))
                .andExpect(status().isNotFound());
    }

    private JsonNode create(String url, String json) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private String articleJson(long sectionId, long authorId, String title) {
        return "{\"sectionId\":" + sectionId + ",\"authorId\":" + authorId
                + ",\"title\":\"" + title + "\",\"text\":\"Full text of " + title
                + "\",\"status\":\"DRAFT\"}";
    }
}

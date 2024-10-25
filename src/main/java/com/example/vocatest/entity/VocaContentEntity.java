package com.example.vocatest.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter @Setter
@NoArgsConstructor
public class VocaContentEntity { //단어 내용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "단어", example = "apple")
    private String text; // 원본

    @Schema(description = "단어 해석", example = "사과")
    private String transtext; //해석

    @Schema(description = "예시 문장", example = "I eat apple")
    private String sampleSentence; // 예문

    @Schema(description = "단어장 참조값")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocalist_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 부모 엔티티가 삭제되면 자식 엔티티도 삭제
    private VocaListEntity vocaListEntity;

    @Version
    private Long version;

//    생성자
    public VocaContentEntity(String text, String transtext, String sampleSentence, VocaListEntity vocaListEntity) {
        this.text = text;
        this.transtext = transtext;
        this.sampleSentence = sampleSentence;
        this.vocaListEntity = vocaListEntity;
    }


}

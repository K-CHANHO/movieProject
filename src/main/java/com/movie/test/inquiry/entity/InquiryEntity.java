package com.movie.test.inquiry.entity;

import com.movie.test.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "mv_inquiry")
@SQLDelete(sql = "UPDATE mv_inquiry SET isDeleted = true, deleteDate = now() WHERE inquiryId = ?")
@Where(clause = "isDeleted is null || isDeleted = 0")
public class InquiryEntity extends BaseTimeEntity {

    @Id
    @Column
    private String inquiryId; // 문의사항 id

    @Column
    private String userId; // 작성자 id

    @Column
    private String category; // 문의유형

    @Column
    private String title; // 문의제목

    @Column
    private byte[] content; // 문의내용

    @Column
    private String userEmail; // 답변받을 email 주소

    @Column
    @ColumnDefault("'N'")
    private String answerYN; // 답변 여부


}

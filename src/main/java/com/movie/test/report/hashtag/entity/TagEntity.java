package com.movie.test.report.hashtag.entity;

import com.movie.test.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@DynamicUpdate
@Table(name = "mv_hashtag")
@SQLDelete(sql = "UPDATE mv_hashtag SET isDeleted = true, deleteDate = now() WHERE tagId = ?")
public class TagEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column
    private String content;
}

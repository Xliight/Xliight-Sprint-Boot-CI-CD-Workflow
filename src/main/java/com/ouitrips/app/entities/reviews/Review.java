package com.ouitrips.app.entities.reviews;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "reviews", schema = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_gen")
    @SequenceGenerator(name = "reviews_id_gen", sequenceName = "reviews_pkid_review_seq", allocationSize = 1, schema = "reviews")
    @Column(name = "pkid_notice")
    private Integer id;
    @Column(name = "note")
    private String note;
    @Column(name = "review_date")
    private Instant reviewDate;
    @Column(name = "visibility")
    private Boolean visibility;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "recommended")
    private Boolean recommended;
    @Column(name = "archive")
    private Boolean archive;
    @Column(name = "comment")
    private String comment;
    @Column(name = "rating")
    private Integer rating;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_type")
    private FeedbackType fkidType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_reason")
    private Reason fkidReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_waiting_time")
    private WaitingTime fkidWaitingTime;
}

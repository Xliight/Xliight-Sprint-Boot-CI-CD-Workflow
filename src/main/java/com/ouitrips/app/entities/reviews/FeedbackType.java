package com.ouitrips.app.entities.reviews;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "feedback_types", schema = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_types_id_gen")
    @SequenceGenerator(name = "feedback_types_id_gen", sequenceName = "feedback_types_pkid_type_seq", allocationSize = 1, schema = "reviews")
    @Column(name = "pkid_type")
    private Integer id;
    @Column(name = "label")
    private String label;
    @Column(name = "order_feedback")
    private Integer order;
    @Column(name = "status")
    private Boolean status;
}

package com.ouitrips.app.entities.geolocation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "response_api",schema="geolocation")
public class ResponseAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "response_id_responseApi")
    @SequenceGenerator(name = "response_id_responseApi", sequenceName = "response_api_pkid_response_seq", allocationSize = 1,schema = "geolocation")
    @Column(name ="pkid_response" )
    private Integer id;

    @Column(name = "json_response", columnDefinition = "TEXT") // Use TEXT for large data
    private String jsonResponse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkid_request")
    @JsonIgnore // Prevent serialization of this reference

    private RequestAPI requestAPI;


}
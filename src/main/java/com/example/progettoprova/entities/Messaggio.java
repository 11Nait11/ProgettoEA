package com.example.progettoprova.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Messaggio {

    @Id
    @GeneratedValue
    private Long id;


    @Column(columnDefinition = "TEXT")//  > 256 caratteri
    private String testo;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataInvio;

    @ManyToOne
    @JoinColumn(name = "mittente_id")
    private Utente mittente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Utente destinatario;



}

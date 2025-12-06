package com.projeto_integrado_biblioteca.domains.session;

import com.projeto_integrado_biblioteca.domains.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_sessions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.LENDO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private SessionBook book;
}

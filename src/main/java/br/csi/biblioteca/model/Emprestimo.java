package br.csi.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_emp;

//    @NonNull
//    private int id_livro_emp;
    //mapeia relacionamento com livro
    @ManyToOne
    @JoinColumn(name = "id_livro_emp")
    private Livro livro_emp;

//    @NonNull
//    private int id_usuario_emp;
    //mapeia relacionamento com usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario_emp")
    private Usuario usuario_emp;

    @NonNull
    private Date data_emprestimo_emp;
    @NonNull
    private Date data_devolucao_prevista_emp;

    private Date data_devolucao_efetiva_emp;
    @NonNull
    private String status_emp;

    //extras - para exibir - acessado pelos relacionamentos
//    @NonNull
//    private String titulo_livro;
//    @NonNull
//    private String nome_usuario;
}

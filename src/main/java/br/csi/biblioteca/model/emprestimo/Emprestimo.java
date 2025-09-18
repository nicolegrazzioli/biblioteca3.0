package br.csi.biblioteca.model.emprestimo;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_emp;

//    @NonNull
//    private int id_livro_emp;
    //mapeia relacionamento com livro
    @ManyToOne
    @JoinColumn(name = "id_livro_emp")
    private Livro livroEmp;

//    @NonNull
//    private int id_usuario_emp;
    //mapeia relacionamento com usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario_emp")
    private Usuario usuarioEmp;

    @Column(name = "data_emprestimo_emp", nullable = false)
    private LocalDate dataEmprestimoEmp;

    @Column(name = "data_devolucao_prevista_emp", nullable = false)
    private LocalDate dataDevolucaoPrevistaEmp;

    @Column(name = "data_devolucao_efetiva_emp")
    private LocalDate dataDevolucaoEfetivaEmp;

    @Column(name = "status_emp", nullable = false)
    private String statusEmp;

    //extras - para exibir - acessado pelos relacionamentos
//    @NonNull
//    private String titulo_livro;
//    @NonNull
//    private String nome_usuario;
}

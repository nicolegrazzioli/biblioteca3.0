package br.csi.biblioteca.model.emprestimo;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

/**
 * @NotBlank -- strings (valor valido)
 * @NotNull -- não strings
 * @Email
 * @Size(min, max)
 * @Past -- data informada deve ser passado
 */
@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emp")
    private int idEmp;

    // emprestimo - livro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro_emp")
    private Livro livroEmp;

    // emprestimo - usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_emp")
    private Usuario usuarioEmp;

    @NotNull(message = "O empréstimo deve ser iniciado hoje")
    @Column(name = "data_emprestimo_emp", nullable = false)
    private LocalDate dataEmprestimoEmp;

    @NotNull(message = "O empréstimo deve ser devolvido daqui 14 dias ou renovado")
    @Column(name = "data_devolucao_prevista_emp", nullable = false)
    private LocalDate dataDevolucaoPrevistaEmp;

    @Column(name = "data_devolucao_efetiva_emp")
    private LocalDate dataDevolucaoEfetivaEmp;

    @NotNull(message = "O status deve ser: ativo, atrasado ou concluido")
    @Column(name = "status_emp", nullable = false)
    private String statusEmp;

}

package br.csi.biblioteca.model.emprestimo;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID único do empréstimo", example = "1")
    private int idEmp;

    // emprestimo - livro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro_emp")
    @Schema(description = "ID único do livro do empréstimo", example = "2")
    private Livro livroEmp;

    // emprestimo - usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_emp")
    @Schema(description = "ID único do usuário do empréstimo", example = "3")
    private Usuario usuarioEmp;

    @NotNull(message = "O empréstimo deve ser iniciado hoje")
    @Column(name = "data_emprestimo_emp", nullable = false)
    @Schema(description = "Data de início do empréstimo (data atual)", example = "11/08/2025")
    private LocalDate dataEmprestimoEmp;

    @NotNull(message = "O empréstimo deve ser devolvido daqui 14 dias ou renovado")
    @Column(name = "data_devolucao_prevista_emp", nullable = false)
    @Schema(description = "Data prevista para devolução do empréstimo (14 dias após a data de início)", example = "25/08/2025")
    private LocalDate dataDevolucaoPrevistaEmp;

    @Column(name = "data_devolucao_efetiva_emp")
    @Schema(description = "Data de devolução efetiva do empréstimo", example = "25/08/2025")
    private LocalDate dataDevolucaoEfetivaEmp;

    @NotNull(message = "O status deve ser: ativo, atrasado ou concluido")
    @Column(name = "status_emp", nullable = false)
    @Schema(description = "Identifica o status do empréstimo: ativo, atrasado ou concluído", example = "ativo")
    private String statusEmp;

}

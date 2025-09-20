package br.ufrn.imd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.Csv;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    @DisplayName("Deve consolidar corretamente o status do aluno como aprovado")
    public void testaConsolidacaoDeNotaAlunoStatusAprovado() {
        Docente docente = new Docente();
        Disciplina disciplina = new Disciplina();
        Matricula matricula = new Matricula(new Discente(), new Turma(docente, disciplina));

        BigDecimal nota1 = new BigDecimal("8.5");
        BigDecimal nota2 = new BigDecimal("7.0");
        BigDecimal nota3 = new BigDecimal("9.0");

        matricula.cadastrarNota1(nota1);
        matricula.cadastrarNota2(nota2);
        matricula.cadastrarNota3(nota3);

        matricula.cadastrarFrequencia(87);

        matricula.consolidarParcialmente();
        StatusAprovacao statusAprovacaoEsperado = StatusAprovacao.APR;
        Assertions.assertEquals(statusAprovacaoEsperado, matricula.getStatus());
    }

    @ParameterizedTest(name = "Entradas: nota1={0}, nota2={1}, nota3={2}, frequencia={3}, statusEsperado={4}")
    @CsvSource(value = {
            "8.5,7.0,9.0,87,APR",
            "2.5,7.0,9.0,87,REC",
            "8.0, 8.0, 10.0, 70, REPF",
            "2.0, 3.0, 3.0, 70, REPMF",
            "2.0, 3.0, 3.0, 87, REP",
    })
    @DisplayName("Deve consolidar corretamente o status do aluno com base nas notas e frequência")
    public void testaConsolidacaoDeNota(BigDecimal nota1 , BigDecimal nota2, BigDecimal nota3, Integer frequencia, StatusAprovacao statusEsperado) {
        Docente docente = new Docente();
        Disciplina disciplina = new Disciplina();
        Matricula matricula = new Matricula(new Discente(), new Turma(docente, disciplina));

        matricula.cadastrarNota1(nota1);
        matricula.cadastrarNota2(nota2);
        matricula.cadastrarNota3(nota3);

        matricula.cadastrarFrequencia(frequencia);

        matricula.consolidarParcialmente();
        Assertions.assertEquals(statusEsperado, matricula.getStatus());
    }

    @ParameterizedTest(name = "Entradas: nota1={0}, nota2={1}, nota3={2}, frequencia={3}")
    @CsvSource(value = {
            "-1.0,7.0,9.0,87",
            "8.5,11.0,9.0,87",
            "8.5,7.0,-3.0,87",
            "8.5,7.0,9.0,-10",
            "8.5,7.0,9.0,150",
    })
    @DisplayName("Deve lançar IllegalArgumentException para notas ou frequência inválidas")
    public void testaNotasEFrequenciaInvalidas(BigDecimal nota1 , BigDecimal nota2, BigDecimal nota3, Integer frequencia) {
        Docente docente = new Docente();
        Disciplina disciplina = new Disciplina();
        Matricula matricula = new Matricula(new Discente(), new Turma(docente, disciplina));

        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            matricula.cadastrarNota1(nota1);
            matricula.cadastrarNota2(nota2);
            matricula.cadastrarNota3(nota3);
            matricula.cadastrarFrequencia(frequencia);
        });
    }
}

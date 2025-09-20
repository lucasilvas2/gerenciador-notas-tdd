package br.ufrn.imd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.Csv;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
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

    @ParameterizedTest
    @CsvSource(value = {
            "8.5,7.0,9.0,87,APR",
            "2.5,7.0,9.0,87,REC",
            "8.0, 8.0, 10.0, 70, REPF",
            "2.0, 3.0, 3.0, 70, REPMF",
            "2.0, 3.0, 3.0, 87, REP",
    })
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
}

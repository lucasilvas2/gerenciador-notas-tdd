package br.ufrn.imd;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Matricula {
	private final Discente discente;
	
	private final Turma turma;
	
	private BigDecimal nota1;

	private BigDecimal nota2;

	private BigDecimal nota3;

	private Integer frequencia;
	
	private StatusAprovacao status;

    //const
    private static final int MIN_FREQUENCIA_PARA_APROVACAO = 75;
    private static final String VALOR_NOTA_MINIMA = "4.0";

	Matricula(Discente discente, Turma turma) {
		this.discente = discente;
		this.turma = turma;
	}

	public BigDecimal getNota1() {
		return nota1;
	}

	public void cadastrarNota1(BigDecimal nota1) {
		this.nota1 = nota1;
	}

	public BigDecimal getNota2() {
		return nota2;
	}

	public void cadastrarNota2(BigDecimal nota2) {
		this.nota2 = nota2;
	}

	public BigDecimal getNota3() {
		return nota3;
	}

	public void cadastrarNota3(BigDecimal nota3) {
		this.nota3 = nota3;
	}

	public Integer getFrequencia() {
		return frequencia;
	}

	public void cadastrarFrequencia(Integer frequencia) {
		this.frequencia = frequencia;
	}

	public Discente getDiscente() {
		return discente;
	}

	public Turma getTurma() {
		return turma;
	}

    public void consolidarParcialmente() {
        BigDecimal media = this.calcularMedia();
        boolean algumaNotaAbaixoDeQuatro = this.temAlgumaNotaAbaixoDeQuatro();
        boolean frequenciaMinima = this.temFrequenciaMinima();

        if (!frequenciaMinima) {
            if (media.compareTo(new BigDecimal("3.0")) < 0) {
                this.setStatus(StatusAprovacao.REPMF);
            } else {
                this.setStatus(StatusAprovacao.REPF);
            }
            return;
        }

        if (media.compareTo(new BigDecimal("6.0")) >= 0 && !algumaNotaAbaixoDeQuatro) {
            this.setStatus(StatusAprovacao.APR);
            return;
        }

        if (media.compareTo(new BigDecimal("3.0")) >= 0) {
            this.setStatus(StatusAprovacao.REC);
            return;
        }

        this.setStatus(StatusAprovacao.REP);
    }

    private BigDecimal calcularMedia() {
        BigDecimal soma = BigDecimal.ZERO;
        int count = 0;
        if (this.nota1 != null) {
            soma = soma.add(this.nota1);
            count++;
        }
        if (this.nota2 != null) {
            soma = soma.add(this.nota2);
            count++;
        }
        if (this.nota3 != null) {
            soma = soma.add(this.nota3);
            count++;
        }
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        RoundingMode roundingMode = RoundingMode.HALF_EVEN;
        return soma.divide(new BigDecimal(count), 2, roundingMode);
    }

    private boolean temAlgumaNotaAbaixoDeQuatro() {
        return (this.nota1 != null && this.nota1.compareTo(new BigDecimal(VALOR_NOTA_MINIMA)) < 0) ||
                (this.nota2 != null && this.nota2.compareTo(new BigDecimal(VALOR_NOTA_MINIMA)) < 0) ||
                (this.nota3 != null && this.nota3.compareTo(new BigDecimal(VALOR_NOTA_MINIMA)) < 0);
    }

    private boolean temFrequenciaMinima() {
        return this.frequencia != null && this.frequencia >= MIN_FREQUENCIA_PARA_APROVACAO;
    }

	public StatusAprovacao getStatus() {
		return status;
	}

	private void setStatus(StatusAprovacao status) {
		this.status = status;
	}
}
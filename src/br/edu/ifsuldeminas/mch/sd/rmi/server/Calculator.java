package br.edu.ifsuldeminas.mch.sd.rmi.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.edu.ifsuldeminas.mch.sd.rmi.remote.Operations;

public class Calculator implements Operations {

	private List<String> lastOperations = new ArrayList<String>();

	// --- Operações básicas ---

	public Number sum(Number x, Number y) {
		Number result = x.doubleValue() + y.doubleValue();
		log(x + " + " + y + " = " + result);
		return result;
	}

	public Number sub(Number x, Number y) {
		Number result = x.doubleValue() - y.doubleValue();
		log(x + " - " + y + " = " + result);
		return result;
	}

	public Number mul(Number x, Number y) {
		Number result = x.doubleValue() * y.doubleValue();
		log(x + " * " + y + " = " + result);
		return result;
	}

	public Number div(Number x, Number y) {
		Number result = Double.NaN;
		if (y.doubleValue() != 0)
			result = x.doubleValue() / y.doubleValue();
		log(x + " / " + y + " = " + result);
		return result;
	}

	// --- Raiz ---

	public Number sqrt(Number x) {
		Number result = Math.sqrt(x.doubleValue());
		log("sqrt(" + x + ") = " + result);
		return result;
	}

	public Number nthRoot(Number x, Number n) {
		Number result = Math.pow(x.doubleValue(), 1.0 / n.doubleValue());
		log("root(" + n + ", " + x + ") = " + result);
		return result;
	}

	// --- Potência ---

	public Number pow(Number base, Number exp) {
		Number result = Math.pow(base.doubleValue(), exp.doubleValue());
		log(base + " ^ " + exp + " = " + result);
		return result;
	}

	// --- Porcentagem ---

	public Number percentage(Number value, Number percent) {
		Number result = (value.doubleValue() * percent.doubleValue()) / 100.0;
		log(percent + "% de " + value + " = " + result);
		return result;
	}

	// --- Módulo ---

	public Number mod(Number x, Number y) {
		if (y.doubleValue() == 0) {
			log(x + " % " + y + " = NaN (divisão por zero)");
			return Double.NaN;
		}
		Number result = x.longValue() % y.longValue();
		log(x + " % " + y + " = " + result);
		return result;
	}

	// --- Fatorial ---

	public long factorial(long n) throws ArithmeticException {
		if (n < 0)
			throw new ArithmeticException("Fatorial indefinido para números negativos.");
		if (n > 20)
			throw new ArithmeticException("Fatorial de " + n + " excede o limite de long (máx: 20!).");
		long result = 1;
		for (long i = 2; i <= n; i++)
			result *= i;
		log(n + "! = " + result);
		return result;
	}

	// --- Conversões ---

	public String decimalToBinary(long n) {
		String result = Long.toBinaryString(n);
		log("dec(" + n + ") -> bin = " + result);
		return result;
	}

	public String decimalToHex(long n) {
		String result = Long.toHexString(n).toUpperCase();
		log("dec(" + n + ") -> hex = " + result);
		return result;
	}

	public long binaryToDecimal(String binary) {
		long result = Long.parseLong(binary, 2);
		log("bin(" + binary + ") -> dec = " + result);
		return result;
	}

	public long hexToDecimal(String hex) {
		long result = Long.parseLong(hex, 16);
		log("hex(" + hex + ") -> dec = " + result);
		return result;
	}

	public String binaryToHex(String binary) {
		long decimal = Long.parseLong(binary, 2);
		String result = Long.toHexString(decimal).toUpperCase();
		log("bin(" + binary + ") -> hex = " + result);
		return result;
	}

	public String hexToBinary(String hex) {
		long decimal = Long.parseLong(hex, 16);
		String result = Long.toBinaryString(decimal);
		log("hex(" + hex + ") -> bin = " + result);
		return result;
	}

	// --- Histórico ---

	public List<String> lastOperations(int howMany) {
		if (lastOperations.size() < howMany)
			return lastOperations();
		return new ArrayList<String>(lastOperations.subList(
				lastOperations.size() - howMany, lastOperations.size()));
	}

	public List<String> lastOperations() {
		return new ArrayList<>(lastOperations);
	}

	private void log(String entry) {
		lastOperations.add(entry);
		System.out.printf("%s at %s\n", entry, new Date());
	}
}

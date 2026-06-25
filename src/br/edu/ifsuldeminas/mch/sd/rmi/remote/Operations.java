package br.edu.ifsuldeminas.mch.sd.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Operations extends Remote {
	// Operações básicas
	Number sum(Number x, Number y) throws RemoteException;
	Number sub(Number x, Number y) throws RemoteException;
	Number mul(Number x, Number y) throws RemoteException;
	Number div(Number x, Number y) throws RemoteException;

	// Raiz (quadrada, cúbica, etc)
	Number sqrt(Number x) throws RemoteException;
	Number nthRoot(Number x, Number n) throws RemoteException;

	// Potência
	Number pow(Number base, Number exp) throws RemoteException;

	// Porcentagem
	Number percentage(Number value, Number percent) throws RemoteException;

	// Módulo
	Number mod(Number x, Number y) throws RemoteException;

	// Fatorial
	long factorial(long n) throws RemoteException, ArithmeticException;

	// Conversões Decimal <> Binário <> Hexadecimal
	String decimalToBinary(long n) throws RemoteException;
	String decimalToHex(long n) throws RemoteException;
	long binaryToDecimal(String binary) throws RemoteException;
	long hexToDecimal(String hex) throws RemoteException;
	String binaryToHex(String binary) throws RemoteException;
	String hexToBinary(String hex) throws RemoteException;

	// Histórico
	List<String> lastOperations(int howMany) throws RemoteException;
	List<String> lastOperations() throws RemoteException;
}

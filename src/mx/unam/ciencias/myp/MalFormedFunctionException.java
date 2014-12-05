package mx.unam.ciencias.myp;

public class MalFormedFunctionException extends Exception {
	  public MalFormedFunctionException() { super(); }
	  public MalFormedFunctionException(String message) { super(message); }
	  public MalFormedFunctionException(String message, Throwable cause) { super(message, cause); }
	  public MalFormedFunctionException(Throwable cause) { super(cause); }
}

package generator;

public class ImError extends Exception {

    public String getMessage() {
		return "Result of Inverse Fourier Transform has imaginary part";
    }
}

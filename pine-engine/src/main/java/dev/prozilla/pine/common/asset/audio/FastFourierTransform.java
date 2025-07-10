package dev.prozilla.pine.common.asset.audio;

public final class FastFourierTransform {

	private FastFourierTransform() {}
	
	public static void fft(double[] real, double[] imag) {
		int n = real.length;
		if (n == 1) return;
		
		// Divide
		double[] evenReal = new double[n / 2];
		double[] evenImag = new double[n / 2];
		double[] oddReal = new double[n / 2];
		double[] oddImag = new double[n / 2];
		
		for (int i = 0; i < n / 2; i++) {
			evenReal[i] = real[2 * i];
			evenImag[i] = imag[2 * i];
			oddReal[i] = real[2 * i + 1];
			oddImag[i] = imag[2 * i + 1];
		}
		
		// Recurse
		fft(evenReal, evenImag);
		fft(oddReal, oddImag);
		
		// Combine
		for (int k = 0; k < n / 2; k++) {
			double angle = -2 * Math.PI * k / n;
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			
			double tre = cos * oddReal[k] - sin * oddImag[k];
			double tim = sin * oddReal[k] + cos * oddImag[k];
			
			real[k] = evenReal[k] + tre;
			imag[k] = evenImag[k] + tim;
			real[k + n / 2] = evenReal[k] - tre;
			imag[k + n / 2] = evenImag[k] - tim;
		}
	}
	
	public static double[] magnitude(double[] real, double[] imag) {
		int n = real.length;
		double[] mag = new double[n];
		for (int i = 0; i < n; i++) {
			mag[i] = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
		}
		return mag;
	}
	
}

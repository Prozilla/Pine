package dev.prozilla.pine.common.system;

/**
 * British version of {@link Color}.
 */
public class Colour extends Color {
	
	public Colour() {
		super();
	}
	
	public Colour(java.awt.Color color) {
		super(color);
	}
	
	public Colour(float red, float green, float blue) {
		super(red, green, blue);
	}
	
	public Colour(float red, float green, float blue, float alpha) {
		super(red, green, blue, alpha);
	}
	
	public Colour(int red, int green, int blue) {
		super(red, green, blue);
	}
	
	public Colour(int red, int green, int blue, int alpha) {
		super(red, green, blue, alpha);
	}
	
	@Override
	public Colour toColour() {
		return this;
	}
	
	/**
	 * @see Color#hsl(int, int, int)
	 */
	public static Colour hsl(int hue, int saturation, int lightness) {
		return Color.hsl(hue, saturation, lightness).toColour();
	}
	
	/**
	 * @see Color#hsl(float, float, float)
	 */
	public static Colour hsl(float hue, float saturation, float lightness) {
		return Color.hsl(hue, saturation, lightness).toColour();
	}
	
	/**
	 * @see Color#decode
	 */
	public static Colour decode(String input) throws NumberFormatException {
		return Color.decode(input).toColour();
	}
	
	public static Colour white() {
		return Color.white().toColour();
	}
	
	public static Colour black() {
		return Color.black().toColour();
	}
	
	public static Colour red() {
		return Color.red().toColour();
	}
	
	public static Colour green() {
		return Color.green().toColour();
	}
	
	public static Colour blue() {
		return Color.blue().toColour();
	}
	
	public static Colour transparent() {
		return Color.transparent().toColour();
	}
	
	public static Colour aqua() {
		return Color.aqua().toColour();
	}
	
	public static Colour cyan() {
		return Color.cyan().toColour();
	}
	
	public static Colour darkBlue() {
		return Color.darkBlue().toColour();
	}
	
	public static Colour darkCyan() {
		return Color.darkCyan().toColour();
	}
	
	public static Colour darkGrey() {
		return Color.darkGray().toColour();
	}
	
	public static Colour darkGreen() {
		return Color.darkGreen().toColour();
	}
	
	public static Colour darkMagenta() {
		return Color.darkMagenta().toColour();
	}
	
	public static Colour darkOrange() {
		return Color.darkOrange().toColour();
	}
	
	public static Colour darkRed() {
		return Color.darkRed().toColour();
	}
	
	public static Colour dimGrey() {
		return Color.dimGray().toColour();
	}
	
	public static Colour gold() {
		return Color.gold().toColour();
	}
	
	public static Colour grey() {
		return Color.gray().toColour();
	}
	
	public static Colour lightBlue() {
		return Color.lightBlue().toColour();
	}
	
	public static Colour lightCyan() {
		return Color.lightCyan().toColour();
	}
	
	public static Colour lightGrey() {
		return Color.lightGray().toColour();
	}
	
	public static Colour lightGreen() {
		return Color.lightGreen().toColour();
	}
	
	public static Colour lightYellow() {
		return Color.lightYellow().toColour();
	}
	
	public static Colour lime() {
		return Color.lime().toColour();
	}
	
	public static Colour magenta() {
		return Color.magenta().toColour();
	}
	
	public static Colour maroon() {
		return Color.maroon().toColour();
	}
	
	public static Colour mediumBlue() {
		return Color.mediumBlue().toColour();
	}
	
	public static Colour mintCream() {
		return Color.mintCream().toColour();
	}
	
	public static Colour navy() {
		return Color.navy().toColour();
	}
	
	public static Colour olive() {
		return Color.olive().toColour();
	}
	
	public static Colour orange() {
		return Color.orange().toColour();
	}
	
	public static Colour orangeRed() {
		return Color.orangeRed().toColour();
	}
	
	public static Colour pink() {
		return Color.pink().toColour();
	}
	
	public static Colour purple() {
		return Color.purple().toColour();
	}
	
	public static Colour rebeccaPurple() {
		return Color.rebeccaPurple().toColour();
	}
	
	public static Colour silver() {
		return Color.silver().toColour();
	}
	
	public static Colour skyBlue() {
		return Color.skyBlue().toColour();
	}
	
	public static Colour springGreen() {
		return Color.springGreen().toColour();
	}
	
	public static Colour teal() {
		return Color.teal().toColour();
	}
	
	public static Colour tomato() {
		return Color.tomato().toColour();
	}
	
	public static Colour turquoise() {
		return Color.turquoise().toColour();
	}
	
	public static Colour violet() {
		return Color.violet().toColour();
	}
	
	public static Colour whiteSmoke() {
		return Color.whiteSmoke().toColour();
	}
	
	public static Colour yellow() {
		return Color.yellow().toColour();
	}
	
}

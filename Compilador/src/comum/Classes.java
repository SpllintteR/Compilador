package comum;

public enum Classes {
	;

	public static String get(int id) {
		return values[id];
	}
	
	private static String[] values = new String[]{
	"EPSILON", "DOLLAR", "identificador", "constante integer", "constante float", "constante string",
	"s�mbolo especial", "s�mbolo especial", "s�mbolo especial", "s�mbolo especial", "s�mbolo especial",
	"s�mbolo especial", "s�mbolo especial", "s�mbolo especial", "s�mbolo especial", "s�mbolo especial",
	"s�mbolo especial", "s�mbolo especial", "s�mbolo especial", "s�mbolo especial", "s�mbolo especial",
	"s�mbolo especial", "s�mbolo especial", "s�mbolo especial",
	"palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada",
	"palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada",
	"palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada", "palavra reservada"};
}
